package com.douzone.douzone_BE.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

import com.douzone.douzone_BE.service.inter.EnrollService;
import com.douzone.douzone_BE.dto.EnrollRequest;
import com.douzone.douzone_BE.dto.EnrollmentAvailableResponse;
import com.douzone.douzone_BE.repository.TeacherRepository;
import com.douzone.douzone_BE.repository.SubjectRepository;
import com.douzone.douzone_BE.dto.EnrollmentInfoDto;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/enroll")
@Tag(name = "수강신청 API", description = "수강신청 및 강사/과목 조회 관련 API")
@RequiredArgsConstructor
public class EnrollController {

    private final EnrollService enrollService;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;

    // 선택한 강사의 강의 가능한 공강시간을 반환한다.
    // 월수금, 화목 요일을 선택적으로 받아 해당 요일의 가능시간을 반환한다.
    // 수업은 25분 기준으로 가능한 시작 시간만을 받아 반환한다.
    @Operation(summary = "강사 공강 시작 시간 조회", description = "강사 ID 또는 이름과 요일(월수금/화목)을 받아 해당 강사의 공강 시작 시간 목록을 반환합니다. 시작 시간은 25분 단위로 반환됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = java.time.LocalTime.class)))),
            @ApiResponse(responseCode = "404", description = "강사를 찾을 수 없음")
    })
    @GetMapping("/availableTime")
    public ResponseEntity<?> availableTime(
            @Parameter(description = "강사 ID (숫자)", example = "1") @RequestParam Long teacherId,
            @Parameter(description = "조회할 요일 그룹: '월수금' 또는 '화목'", example = "135") @RequestParam(required = false) String day) {
        try {
            List<LocalTime> times = enrollService.getTeacherAvailableStartTimesByID(teacherId, day);
            return ResponseEntity.ok(times);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    // 선택한 과목의 강의 가능한 강사 목록을 반환한다.
    // 해당 분반을 가지고 있는 강사의 목록이다.
    @Operation(summary = "과목별 담당 강사 조회", description = "과목 id를 받아 해당 과목을 담당하는 강사의 분반 목록(강사 이름, 분반 ID 등)을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnrollmentAvailableResponse.class))))
    })
    @GetMapping("/availableTeacher")
    public ResponseEntity<?> availableTeacher(
            @Parameter(description = "과목 id", example = "1") @RequestParam Long subjectId) {
        // 모든 과목-강사 목록에서 해당 과목만 필터링하여 반환
        List<EnrollmentAvailableResponse> all = enrollService.getAvailableClassesAndTeachers();
        Map<String, Long> filtered = all.stream()
                .filter(e -> e.getSubjectId() != null && e.getSubjectId().equals(subjectId))
                .flatMap(e -> e.getTeachers().stream()) // List<TeacherInfo> 풀기
                .collect(Collectors.toMap(
                        EnrollmentAvailableResponse.TeacherInfo::getTeacherName, // key: 이름
                        EnrollmentAvailableResponse.TeacherInfo::getTeacherId, // value: ID
                        (existing, replacement) -> existing // 중복 이름 처리: 기존 값 유지
                ));

        if (filtered.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 과목을 담당하는 강사를 찾을 수 없습니다: " + subjectId);
        }
        return ResponseEntity.ok(filtered);
    }

    // 현재 수업 가능한 과목들을 반환한다.
    @Operation(summary = "수업 가능 과목 목록 조회", description = "현재 수강 신청이 가능한 과목 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class))))
    })
    @GetMapping("/availableSubject")
    public ResponseEntity<?> getAvailableSubject() {
        List<EnrollmentAvailableResponse> all = enrollService.getAvailableClassesAndTeachers();
        // 과목만 추출
        Map<String, Long> subjectMap = all.stream()
                .collect(Collectors.toMap(
                        EnrollmentAvailableResponse::getSubjectName,
                        EnrollmentAvailableResponse::getSubjectId,
                        (existing, replacement) -> existing // 중복 키 발생 시 기존 값 유지
                ));
        return ResponseEntity.ok(subjectMap);
    }

    // 분반이 유효한 분반이고, 강사 가능 시간이 공강이라면
    // 강사 가능 시간의 status를 1로 수정하고, 분반과 학생 번호를 넣는다.
    @Operation(summary = "수강 신청", description = "학생 ID, 강사, 과목, 요일 리스트를 받아 수강 신청을 수행합니다. 분반이 유효하고 강사 시간이 공강일 경우 예약이 완료됩니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수강신청 요청 정보", required = true, content = @Content(schema = @Schema(implementation = EnrollRequest.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수강 신청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(요청 파라미터 누락 등)"),
            @ApiResponse(responseCode = "409", description = "이미 예약된 시간 등 충돌 발생")
    })
    @PostMapping("/enroll")
    public ResponseEntity<String> postEnrollment(@RequestBody Map<String, Object> body) {
        try {
            Long studentId = parseLongField(body, "studentId");
            Long teacherId = parseLongField(body, "teacherId");
            Long subjectId = parseLongField(body, "subjectId");

            List<Integer> dayList = parseDaysList(body);
            java.time.LocalTime time = parseTimeField(body);

            enrollService.enrollClass(studentId, teacherId, subjectId, dayList, time);

            return ResponseEntity.ok("수강신청 완료");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    private Long parseLongField(Map<String, Object> body, String key) {
        if (!body.containsKey(key) || body.get(key) == null) {
            throw new IllegalArgumentException("필수 필드가 누락되었습니다: " + key);
        }
        Object val = body.get(key);
        try {
            if (val instanceof Number) {
                return ((Number) val).longValue();
            }
            return Long.valueOf(val.toString());
        } catch (Exception ex) {
            throw new IllegalArgumentException("필드 형식이 잘못되었습니다: " + key + " (숫자여야 합니다)");
        }
    }

    private List<Integer> parseDaysList(Map<String, Object> body) {
        Object daysObj = body.get("days");
        if (!(daysObj instanceof java.util.List)) {
            throw new IllegalArgumentException("days는 정수 배열로 전달되어야 합니다. 예: [1,3,5]");
        }
        List<Integer> dayList = new java.util.ArrayList<>();
        for (Object o : (java.util.List<?>) daysObj) {
            if (o instanceof Number) {
                dayList.add(((Number) o).intValue());
            } else {
                try {
                    dayList.add(Integer.valueOf(o.toString()));
                } catch (Exception ex) {
                    throw new IllegalArgumentException("days 리스트는 정수만 포함해야 합니다. 잘못된 값: " + o);
                }
            }
        }
        if (dayList.isEmpty()) {
            throw new IllegalArgumentException("days 리스트에 최소 하나의 요일이 필요합니다.");
        }
        return dayList;
    }

    private java.time.LocalTime parseTimeField(Map<String, Object> body) {
        Object timeObj = body.get("time");
        if (timeObj == null) {
            throw new IllegalArgumentException("time 필드가 필요합니다. 예: '13:00'");
        }
        String timeStr = timeObj.toString();
        try {
            String normalized = timeStr.length() <= 5 ? timeStr : timeStr.substring(0, 5);
            return java.time.LocalTime.parse(normalized);
        } catch (Exception ex) {
            throw new IllegalArgumentException("time 형식이 잘못되었습니다. 'HH:mm' 예: '13:00'");
        }
    }

    @GetMapping("/studentEnrollInfo")
    public ResponseEntity<?> getStudentEnrollInfo(@RequestParam Map<String, Integer> body) {
        Integer studentId = body.get("studentId");
        Integer status = body.get("status");

        List<EnrollmentInfoDto> enrollments = enrollService.getEnrollmentsByStudentIdAndStatus(
                studentId.longValue(), status);

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/teacherEnrollInfo")
    public ResponseEntity<?> getTeacherEnrollInfo(@RequestParam Map<String, Integer> body) {
        Integer teacherId = body.get("teacherId");
        Integer status = body.get("status");

        List<EnrollmentInfoDto> enrollments = enrollService.getEnrollmentsByTeacherIdAndStatus(
                teacherId.longValue(), status);

        return ResponseEntity.ok(enrollments);
    }

}
