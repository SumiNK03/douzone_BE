package com.douzone.douzone_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 식별용 기본 키

    @Column(name = "login_id", length = 50, nullable = false, unique = true)
    private String loginId; // 로그인 아이디

    @Column(name = "login_password", length = 50, nullable = false)
    private String loginPassword; // 로그인 비밀번호

    @Column(name = "student_name", length = 50, nullable = false)
    private String studentName; // 학생 이름

    @Column(name = "phone", length = 20)
    private String phone; // 전화 번호
}