package com.douzone.douzone_BE.dto;

import lombok.*;

// 학생 수강중인 수업 정보 DTO,
// 강사 수업중인 수업 정보 DTO
// 범용으로 사용.

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentInfoDto {
    private Long enrollmentId;
    private Long teacherId;
    private String teacherName;
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private String dayOfWeekPattern; // 요일 패턴
    private java.time.LocalDate startDate; // 수강 시작일
    private java.time.LocalDate endDate; // 수강 종료일
    private Integer status; // 상태 (1=수업 진행중, 0=종료된 수업)
    private Integer count; // 총 수강 횟수
    private Integer leftCount; // 남은 수강 횟수
}



    
