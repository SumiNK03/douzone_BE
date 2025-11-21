package com.douzone.douzone_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 필수
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 식별용 기본 키

    @Column(name = "login_id", length = 50, nullable = false, unique = true)
    private String loginId; // 로그인 아이디

    @Column(name = "login_password", length = 50, nullable = false)
    private String loginPassword; // 로그인 비밀번호

    @Column(name = "teacher_name", length = 50, nullable = false)
    private String teacherName; // 강사 이름

    @Column(name = "phone", length = 20)
    private String phone; // 전화 번호

    // 연관 관계 (class 테이블과의 OneToMany 관계 등은 필요에 따라 추가)
}