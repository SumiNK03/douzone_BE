package com.douzone.douzone_BE.service.inter;

import com.douzone.douzone_BE.dto.EnrollmentAvailableResponse;
import java.util.List;
import java.util.Map;
import com.douzone.douzone_BE.entity.TeacherTime;
import com.douzone.douzone_BE.entity.EnrollmentInfo;
import com.douzone.douzone_BE.dto.EnrollmentInfoDto;
import java.time.LocalTime;

public interface EnrollService {

    /**
     * 1. 과목명과 해당 과목을 담당하는 강사 목록을 조회합니다. (PK 포함)
     * 프론트엔드에서 수강 신청 가능한 목록 형태로 보여주는 용도입니다.
     * @return 과목-강사 구조화된 목록 (DTO)
     */
    List<EnrollmentAvailableResponse> getAvailableClassesAndTeachers();

    /**
     * 2. 강사 이름으로 해당 강사의 공강 시간 목록(시작 시간)을 조회합니다.
     * @param teacherid 강사 아이디
     * @return 강사의 공강 시작 시간 목록 (LocalTime)
     */
    List<LocalTime> getTeacherAvailableStartTimesByID(Long teacherId, String day);
    
    /**
     * 3. 학생이 특정 강사의 특정 분반 수업 시간에 대해 수강 신청(예약)을 합니다.
     * 수정 전, 해당 시간의 status가 0(공강)인지 검사합니다.
     * @param studentId 유저 정보(쿠키값)에서 추출한 학생 ID
     * @param TeacherId 강사 ID
     * @param subjectId 과목 ID
     * @param day 요일 (1=월, 2=화, ..., 5=금)
     * @param time 시작 시간 (LocalTime)
     */
    void enrollClass(Long studentId, Long TeacherId, Long subjectId, List<Integer> days, LocalTime time);

    /**
     * 4. 학생이 특정 강사의 특정 과목 수업에 대해 수강 취소를 합니다.
     * @param studentId 학생 ID
     * @param teacherId 강사 ID
     * @param subjectId 과목 ID
     */
    void cancelEnrollment(Long studentId, Long teacherId, Long subjectId);

    /**
     * 5. 학생 ID로 해당 학생이 수강 신청한 수업 목록을 조회합니다.
     * @param studentId 학생 ID
     * @param status 수업 상태 (1=수업중, 0=종료된 수업)
     * @return 현재 진행 중이거나, 종료된 수업 목록
     */
    List<EnrollmentInfoDto> getEnrollmentsByStudentIdAndStatus(Long studentId, Integer status);

    /**
     * 6. 강사 ID로 해당 강사가 진행중인 수업 목록을 조회합니다.
     * @param teacherId 강사 ID
     * @param status 수업 상태 (1=수업중, 0=종료된 수업)
     * @return 현재 진행 중이거나, 종료된 수업 목록
     */
    List<EnrollmentInfoDto> getEnrollmentsByTeacherIdAndStatus(Long teacherId, Integer status);
}