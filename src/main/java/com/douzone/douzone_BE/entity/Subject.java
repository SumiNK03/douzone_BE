package com.douzone.douzone_BE.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 식별용 기본 키

    @Column(name = "subject_name", length = 100, nullable = false)
    private String subjectName; // 과목 이름
}
