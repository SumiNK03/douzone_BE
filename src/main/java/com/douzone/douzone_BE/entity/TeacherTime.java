package com.douzone.douzone_BE.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "teacher_time")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeacherTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 식별용 기본 키

    // teacher_id (FK -> teacher.id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    // ENUM('월','화','수','목','금','토','일' : 1-7)
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private java.time.LocalTime startTime; // 공강/수업 시작 시간

    @Column(name = "status", nullable = false)
    private Integer status; // 해당 시간이 수업 중(1)인지, 공강(0) 인지 (TINYINT(1) -> Integer or Boolean)

    // class_id (FK -> class.id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id") // NULL 허용
    private Class clazz; // 'class'는 예약어이므로 'clazz' 사용

    // student_id (FK -> student.id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id") // NULL 허용
    private Student student;
    
}
