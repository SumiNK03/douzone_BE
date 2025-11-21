package com.douzone.douzone_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.douzone.douzone_BE.entity.EnrollmentInfo;
import java.util.*;

@Repository
public interface EnrollmentInfoRepository extends JpaRepository<EnrollmentInfo, Long>{
    // 강사 id로 상태가 1인 수강정보를 조회 : 강사 현재 진행중인 수업 확인용
    // DTO 생성 필요. 수강정보로 학생 이름, 과목 이름 + 수강정보 등을 포함하는 DTO

    // 학생 id로 상태가 1인 수강정보를 조회 : 학생의 현재 수강중인 수업 확인용
    // DTO 생성 필요. 수강정보로 강사 이름, 과목 이름 + 수강정보 등을 포함하는 DTO

    // -> 둘 범용으로 EnrollmentInfoDto 생성 완료
    List<EnrollmentInfo> findByTeacherIdAndStatus(Long teacherId, Integer status);
    List<EnrollmentInfo> findByStudentIdAndStatus(Long studentId, Integer status);
    java.util.Optional<EnrollmentInfo> findByStudentIdAndTeacherIdAndSubjectId(Long studentId, Long teacherId, Long subjectId);

}
