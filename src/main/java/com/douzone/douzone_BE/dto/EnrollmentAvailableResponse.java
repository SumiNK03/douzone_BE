package com.douzone.douzone_BE.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class EnrollmentAvailableResponse {
    private Long subjectId;
    private String subjectName;
    private List<TeacherInfo> teachers;

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class TeacherInfo {
        private Long teacherId;
        private String teacherName;
        private Long classId; // 해당 과목을 담당하는 분반 ID (class.id)
    }
}