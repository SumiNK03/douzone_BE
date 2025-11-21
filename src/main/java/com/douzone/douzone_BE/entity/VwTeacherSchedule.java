package com.douzone.douzone_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable; // 읽기 전용 뷰임을 명시 (Hibernate 사용 시)

// vw_teacher_schedule (강사 시간 + 과목 + 학생)
@Entity
@Table(name = "vw_teacher_schedule")
@Immutable // 뷰이므로 변경 불가 설정
@Getter
public class VwTeacherSchedule {

    // 뷰의 복합 키(Primary Key)가 명확하지 않으므로, 유일성이 보장되는 칼럼 조합을 사용하거나
    // @Id 어노테이션을 teacherTimeId에 부여하여 식별자로 사용합니다.
    @Id
    @Column(name = "teacher_time_id", nullable = false)
    private Long teacherTimeId;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private java.time.LocalTime startTime;

    @Column(name = "status", nullable = false)
    private Integer status; // 0=공강, 1=수업중

    @Column(name = "student_id")
    private Long studentId; // NULL 허용

    @Column(name = "student_name")
    private String studentName; // NULL 허용

    @Column(name = "class_id", nullable = false)
    private Long classId;
    
    // 기본 생성자는 JPA가 필요로 하지만, @Immutable의 경우
    // 필드에 @Id가 있다면 @NoArgsConstructor를 생략하기도 합니다.
    // 명시적으로 @NoArgsConstructor(access = AccessLevel.PROTECTED)를 사용하거나
    // DTO처럼 getter만 두는 방식을 권장합니다.
}