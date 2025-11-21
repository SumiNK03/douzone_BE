package com.douzone.douzone_BE.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollRequest {
    private Long studentId;
    private String teacherName;
    private String subjectName;
    private List<Integer> days; // 1=월 ... 7=일
}
