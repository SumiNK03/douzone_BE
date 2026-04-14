# 더존비즈온 화상영어 수강신청 사이트 - 백엔드

더존비즈온 인턴십 프로젝트의 백엔드 서버입니다. DB 설계부터 API 개발까지 전부 담당했습니다.

**API 서버:** [http://douzonesumin.kro.kr:8080](http://douzonesumin.kro.kr:8080)  
**Swagger UI:** [http://douzonesumin.kro.kr:8080/swagger-ui.html](http://douzonesumin.kro.kr:8080/swagger-ui.html)

---

## 목차

- [개요](#개요)
- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [API 명세](#api-명세)
- [설치 및 실행](#설치-및-실행)
- [프로젝트 구조](#프로젝트-구조)
- [데이터베이스](#데이터베이스)

---

## 개요

더존비즈온 인턴십 기간에 개발한 화상영어 수강신청 시스템의 백엔드입니다.

데이터베이스 설계부터 REST API 구현까지 전체 백엔드를 혼자 담당했습니다. Spring Boot를 사용해 RESTful API를 제공하며, JWT 기반 인증으로 보안을 강화했습니다.

**개발 기간:** 2025년 11월 ~ 12월  
**개발자:** SumiNK03

---

## 주요 기능

- **회원 관리** - 강사/학생 회원가입, 로그인, 회원탈퇴
- **강좌 관리** - 수업 가능 과목 조회, 과목별 담당 강사 조회
- **수강신청** - 강좌 신청, 강사 공강 시간 조회
- **강사 기능** - 강사 정보 조회/수정, 강의 일정 조회
- **학생 기능** - 학생 정보 조회/수정, 수강 일정 조회

---

## 기술 스택

**백엔드 프레임워크**
- Spring Boot 3.4.10
- Spring Web
- Spring Data JPA

**인증 & 보안**
- JWT (JSON Web Token)
- JJWT 0.11.5

**데이터베이스**
- MySQL 8.0.33
- Spring JDBC

**개발 도구**
- Java 17
- Gradle
- Lombok
- Springdoc OpenAPI (Swagger UI 2.1.0)

**배포**
- Oracle Cloud (인스턴스)

---

## API 명세

Swagger UI에서 전체 API를 확인하고 테스트할 수 있습니다.

**[Swagger UI 바로가기](http://douzonesumin.kro.kr:8080/swagger-ui.html)**

### 로그인 API

- `POST /login/teacherSignup` - 강사 회원가입
- `POST /login/teacherLogin` - 강사 로그인
- `POST /login/teacherLeave` - 강사 회원탈퇴
- `POST /login/studentSignup` - 학생 회원가입
- `POST /login/studentLogin` - 학생 로그인
- `POST /login/studentLeave` - 학생 회원탈퇴

### 수강신청 API

- `POST /enroll/enroll` - 수강 신청
- `GET /enroll/teacherEnrollInfo` - 강사 수강신청 정보 조회
- `GET /enroll/studentEnrollInfo` - 학생 수강신청 정보 조회
- `GET /enroll/availableTime` - 강사 공강 시간 조회
- `GET /enroll/availableTeacher` - 과목별 담당 강사 조회
- `GET /enroll/availableSubject` - 수업 가능 과목 목록 조회

### 강사 API

- `POST /teacher/edit` - 강사 정보 수정
- `GET /teacher/schedule` - 강사 강의 일정 조회
- `GET /teacher/info` - 강사 정보 조회

### 학생 API

- `POST /student/edit` - 학생 정보 수정
- `GET /student/schedule` - 학생 수강 일정 조회
- `GET /student/info` - 학생 정보 조회

---

## 설치 및 실행

### 필수 요구사항

- Java 17 이상
- MySQL 8.0 이상
- Gradle

### 설치

```bash
git clone https://github.com/SumiNK03/douzone_BE.git
cd douzone_BE
