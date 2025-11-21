package com.douzone.douzone_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

// vw_student_schedule (학생별 수강신청한 수업 시간)
@Entity
@Table(name = "vw_student_schedule")
@Immutable // 뷰이므로 변경 불가 설정
@Getter
public class VwStudentSchedule {

    // teacher_time_id는 teacher_time 테이블의 PK이며,
    // 이 뷰에서 각 행을 유일하게 식별할 수 있는 키로 사용 가능합니다.
    @Id
    @Column(name = "teacher_time_id", nullable = false)
    private Long teacherTimeId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 수업 요일

    @Column(name = "start_time", nullable = false)
    private java.time.LocalTime startTime; // 수업 시작 시간
    
    @Column(name = "is_busy", nullable = false)
    private Integer isBusy; // 상태 (1이 수업중)
}