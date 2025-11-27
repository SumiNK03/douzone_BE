package com.douzone.douzone_BE.service.impl;

import com.douzone.douzone_BE.entity.Class;
import com.douzone.douzone_BE.entity.EnrollmentInfo;
import com.douzone.douzone_BE.entity.Student;
import com.douzone.douzone_BE.entity.Teacher;
import com.douzone.douzone_BE.entity.TeacherTime;
import com.douzone.douzone_BE.entity.Subject;
import com.douzone.douzone_BE.repository.*;
import com.douzone.douzone_BE.dto.*;
import com.douzone.douzone_BE.service.inter.EnrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollServiceImpl implements EnrollService {

        private final TeacherTimeRepository teacherTimeRepository;
        private final ClassRepository classRepository;
        private final TeacherRepository teacherRepository;
        private final SubjectRepository subjectRepository;
        private final StudentRepository studentRepository;
        private final EnrollmentInfoRepository enrollmentInfoRepository;

        // 과목명과 과목을 담당하는 강사들을 조회.
        @Override
        public List<EnrollmentAvailableResponse> getAvailableClassesAndTeachers() {
                // 모든 과목 목록 조회
                List<Subject> subjects = subjectRepository.findAll();

                return subjects.stream()
                                .map(subject -> {
                                        // subjectId로 class 테이블에서 요소 찾기.
                                        List<Class> classes = classRepository.findBySubject_Id(subject.getId());

                                        // class 정보 기반으로 DTO 생성.
                                        List<EnrollmentAvailableResponse.TeacherInfo> teachersInfo = classes.stream()
                                                        .map(clazz -> EnrollmentAvailableResponse.TeacherInfo.builder()
                                                                        .teacherId(clazz.getTeacher().getId())
                                                                        .teacherName(clazz.getTeacher()
                                                                                        .getTeacherName())
                                                                        .classId(clazz.getId())
                                                                        .build())
                                                        .collect(Collectors.toList());

                                        // 최종 응답 DTO 생성
                                        return EnrollmentAvailableResponse.builder()
                                                        .subjectId(subject.getId())
                                                        .subjectName(subject.getSubjectName())
                                                        .teachers(teachersInfo)
                                                        .build();
                                })
                                .collect(Collectors.toList());
        }

        // 강사 id로 해당 강사의 공강 시작 시간 리스트를 조회.
        // day는 "월수금" 또는 "화목"
        @Override
        public List<LocalTime> getTeacherAvailableStartTimesByID(Long teacherId, String day) {
                // 강사 존재 유무 확인
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "해당 강사 ID " + teacherId + "를 찾을 수 없습니다."));

                // status가 0(공강)인 강사 시간대 조회
                List<TeacherTime> availableSlots = teacherTimeRepository.findByTeacher_IdAndStatus(teacherId, 0);
                availableSlots = availableSlots.stream()
                                .filter(slot -> {
                                        if ("월수금".equals(day)) {
                                                return slot.getDayOfWeek() == 1 || slot.getDayOfWeek() == 3
                                                                || slot.getDayOfWeek() == 5;
                                        } else if ("화목".equals(day)) {
                                                return slot.getDayOfWeek() == 2 || slot.getDayOfWeek() == 4;
                                        }
                                        return false;
                                })
                                .collect(Collectors.toList());

                // 시작시간만 추출하여 리스트로 반환
                return availableSlots.stream()
                                .map(TeacherTime::getStartTime)
                                .distinct() // 중복된 시간 제거 (예: 월/화/수 모두 13:00가 공강일 경우)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void enrollClass(Long studentId, Long teacherId, Long subjectId, List<Integer> days, LocalTime time) {

                // 1. 학생, 강사, 과목, 분반 조회
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new NoSuchElementException("학생 ID " + studentId + "를 찾을 수 없습니다."));

                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new NoSuchElementException("강사 ID " + teacherId + "를 찾을 수 없습니다."));

                Subject subject = subjectRepository.findById(subjectId)
                                .orElseThrow(() -> new NoSuchElementException("과목 ID " + subjectId + "를 찾을 수 없습니다."));

                Class clazz = classRepository.findByTeacher_IdAndSubject_Id(teacherId, subjectId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "해당 강사(" + teacherId + ")가 해당 과목(" + subjectId
                                                                + ")을 담당하는 분반을 찾을 수 없습니다."));

                for (Integer day : days) {
                        // 2. 선택한 요일 + 시간에 해당하는 슬롯 조회
                        java.util.Optional<TeacherTime> slotOpt = teacherTimeRepository
                                        .findByTeacher_IdAndDayOfWeekAndStartTime(teacherId, day, time);

                        TeacherTime slot = slotOpt.orElseThrow(() -> new IllegalArgumentException(
                                        "선택한 요일과 시간에 예약 가능한 슬롯이 없습니다."));

                        if (slot.getStatus() != 0) {
                                throw new IllegalStateException(
                                                String.format("이미 예약된 시간입니다. 요일: %d, 시간: %s", day, time));
                        }

                        // 3. 슬롯 상태 수정
                        slot.setStatus(1); // 예약됨
                        slot.setStudent(student);
                        slot.setClazz(clazz);
                }

                // 4. EnrollmentInfo 저장
                LocalDate[] dates = calculateDates(LocalDate.now(), days, 32); // 32회 기준
                enrollmentInfoRepository.save(
                                EnrollmentInfo.builder()
                                                .studentId(studentId)
                                                .teacherId(teacherId)
                                                .subjectId(subjectId)
                                                .dayOfWeekPattern(days.stream()
                                                                .map(String::valueOf)
                                                                .collect(Collectors.joining("")))
                                                .startDate(dates[0])
                                                .endDate(dates[1])
                                                .count(32)
                                                .leftCount(32)
                                                .status(1)
                                                .regDate(LocalDateTime.now())
                                                .build());
        }

        // userId, teacherId, subjectId로 수강 신청 취소.
        @Override
        @Transactional // 데이터 변경이 발생하므로 트랜잭션 설정
        public void cancelEnrollment(Long studentId, Long teacherId, Long subjectId) {
                // 취소할 분반 조회
                Class c = classRepository.findByTeacher_IdAndSubject_Id(teacherId, subjectId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "해당 강사(" + teacherId + ")가 해당 과목(" + subjectId
                                                                + ")을 담당하는 분반을 찾을 수 없습니다."));

                // 취소할 수업의 teacher_time 요소 조회
                List<TeacherTime> slot = teacherTimeRepository.findByTeacher_IdAndClazz_Subject_Id(teacherId,
                                c.getSubject().getId());

                for (TeacherTime st : slot) {
                        // 해당 학생이 예약한 수업인지 및 상태 검증
                        if (st.getStudent() == null || !st.getStudent().getId().equals(studentId)) {
                                throw new SecurityException("해당 학생(" + studentId + ")이 예약한 수업이 아니므로 취소할 수 없습니다.");
                        }

                        if (st.getStatus() != 1) {
                                throw new IllegalStateException("해당 시간은 이미 수업 중이 아니거나 취소된 상태입니다.");
                        }
                }

                EnrollmentInfo enrollmentInfo = enrollmentInfoRepository
                                .findByStudentIdAndTeacherIdAndSubjectId(studentId, teacherId, subjectId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "해당 학생(" + studentId + ")의 수강 정보가 존재하지 않습니다."));

                // 수강 신청 취소 처리. 강사 time 초기 상태로 rollback.
                for (TeacherTime st : slot) {
                        st.setStatus(0); // 공강(0)으로 변경
                        st.setStudent(null); // 학생 정보 초기화
                        st.setClazz(null); // 분반 정보 초기화
                }
                enrollmentInfo.setStatus(0); // 수업 종료 상태로 변경

        }

        @Override
        public List<EnrollmentInfoDto> getEnrollmentsByTeacherIdAndStatus(Long teacherId, Integer status) {
                List<EnrollmentInfo> enrollmentInfoList = enrollmentInfoRepository
                                .findByTeacherIdAndStatus(teacherId, status);

                List<EnrollmentInfoDto> dtoList = new ArrayList<>();

                if (enrollmentInfoList.isEmpty()) {
                        System.out.println("해당 강사(" + teacherId + ")의 수강 정보가 존재하지 않습니다.");
                        return dtoList;
                }

                for (EnrollmentInfo enrollmentInfo : enrollmentInfoList) {
                        Teacher teacher = teacherRepository.findById(enrollmentInfo.getTeacherId())
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "해당 강사 ID " + enrollmentInfo.getTeacherId() + "를 찾을 수 없습니다."));

                        Subject subject = subjectRepository.findById(enrollmentInfo.getSubjectId())
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "해당 과목 ID " + enrollmentInfo.getSubjectId() + "를 찾을 수 없습니다."));

                        Student student = studentRepository.findById(enrollmentInfo.getStudentId())
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "해당 학생 ID " + enrollmentInfo.getStudentId() + "를 찾을 수 없습니다."));

                        EnrollmentInfoDto dto = EnrollmentInfoDto.builder()
                                        .enrollmentId(enrollmentInfo.getId())
                                        .studentId(student.getId())
                                        .studentName(student.getStudentName())
                                        .teacherId(teacher.getId())
                                        .teacherName(teacher.getTeacherName())
                                        .subjectId(subject.getId())
                                        .subjectName(subject.getSubjectName())
                                        .dayOfWeekPattern(enrollmentInfo.getDayOfWeekPattern())
                                        .startDate(enrollmentInfo.getStartDate())
                                        .endDate(enrollmentInfo.getEndDate())
                                        .count(enrollmentInfo.getCount())
                                        .leftCount(enrollmentInfo.getLeftCount())
                                        .status(enrollmentInfo.getStatus())
                                        .build();

                        dtoList.add(dto);
                }

                return dtoList;
        }

        @Override
        public List<EnrollmentInfoDto> getEnrollmentsByStudentIdAndStatus(Long studentId, Integer status) {
                List<EnrollmentInfo> enrollmentInfoList = enrollmentInfoRepository
                                .findByStudentIdAndStatus(studentId, status);

                List<EnrollmentInfoDto> dtoList = new ArrayList<>();

                if (enrollmentInfoList.isEmpty()) {
                        System.out.println("해당 학생(" + studentId + ")의 수강 정보가 존재하지 않습니다.");
                        return dtoList;
                }

                for (EnrollmentInfo enrollmentInfo : enrollmentInfoList) {
                        Teacher teacher = teacherRepository.findById(enrollmentInfo.getTeacherId())
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "해당 강사 ID " + enrollmentInfo.getTeacherId() + "를 찾을 수 없습니다."));

                        Subject subject = subjectRepository.findById(enrollmentInfo.getSubjectId())
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "해당 과목 ID " + enrollmentInfo.getSubjectId() + "를 찾을 수 없습니다."));

                        Student student = studentRepository.findById(enrollmentInfo.getStudentId())
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "해당 학생 ID " + enrollmentInfo.getStudentId() + "를 찾을 수 없습니다."));

                        EnrollmentInfoDto dto = EnrollmentInfoDto.builder()
                                        .enrollmentId(enrollmentInfo.getId())
                                        .studentId(student.getId())
                                        .studentName(student.getStudentName())
                                        .teacherId(teacher.getId())
                                        .teacherName(teacher.getTeacherName())
                                        .subjectId(subject.getId())
                                        .subjectName(subject.getSubjectName())
                                        .dayOfWeekPattern(enrollmentInfo.getDayOfWeekPattern())
                                        .startDate(enrollmentInfo.getStartDate())
                                        .endDate(enrollmentInfo.getEndDate())
                                        .count(enrollmentInfo.getCount())
                                        .leftCount(enrollmentInfo.getLeftCount())
                                        .status(enrollmentInfo.getStatus())
                                        .build();
                        dtoList.add(dto);
                }

                return dtoList;
        }

        /**
         * 시작일과 종료일 계산 (배열 반환)
         * 
         * @param today         오늘 날짜
         * @param days          수업 요일 리스트 (월=1 ~ 일=7)
         * @param totalSessions 총 수업 횟수
         * @return LocalDate[] : [0]=startDate, [1]=endDate
         */
        private static LocalDate[] calculateDates(LocalDate today, List<Integer> days, int totalSessions) {
                if (days == null || days.isEmpty()) {
                        throw new IllegalArgumentException("요일 리스트가 비어 있습니다.");
                }

                int todayDOW = today.getDayOfWeek().getValue(); // 월=1, 일=7

                // 각 요일별 오늘 이후 가장 가까운 날짜 계산
                List<LocalDate> nextDates = days.stream()
                                .map(dow -> today.plusDays((dow - todayDOW + 7) % 7))
                                .collect(Collectors.toList());

                // 시작일 = 가장 가까운 날짜
                LocalDate startDate = nextDates.stream().min(Comparator.naturalOrder()).get();

                // 종료일 = count / 주당 수업 횟수 계산
                int sessionsPerWeek = days.size();
                int totalWeeks = (int) Math.ceil((double) totalSessions / sessionsPerWeek);
                LocalDate endDate = startDate.plusWeeks(totalWeeks - 1);

                return new LocalDate[] { startDate, endDate };
        }
}