package com.douzone.douzone_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enrollment_info")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EnrollmentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 식별용 기본 키

    @Column(name="student_id", nullable = false)
    private Long studentId; // 학생 ID

    @Column(name="teacher_id", nullable = false)
    private Long teacherId; // 강사 ID

    @Column(name="subject_id", nullable = false)
    private Long subjectId; // 과목 ID

    @Column(name="day_of_week_pattern", length = 20, nullable = false)
    private String dayOfWeekPattern; // 요일 패턴

    @Column(name="start_date", nullable = false)
    private java.time.LocalDate startDate; // 수강 시작일

    @Column(name="end_date", nullable = false)
    private java.time.LocalDate endDate; // 수강 종료일

    @Column(name="count", nullable = false)
    private Integer count; // 총 수강 횟수

    @Column(name="left_count")
    private Integer leftCount; // 남은 수강 횟수

    @Column(name="status")
    private Integer status; // 상태 (1=수업 진행중, 0=종료된 수업)

    @Column(name="reg_date", nullable = false, updatable = false)
    private java.time.LocalDateTime regDate; // 등록 일시
}


