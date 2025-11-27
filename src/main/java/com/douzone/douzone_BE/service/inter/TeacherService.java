package com.douzone.douzone_BE.service.inter;
import java.util.Map;

public interface TeacherService {
    // 강사 정보 조회
    public Map<String, String> getTeacherInfo(Long teacherId);
    // 강사 정보 수정
    public boolean updateTeacherInfo(Long teacherId, Map<String, String> teacherInfo);
}
