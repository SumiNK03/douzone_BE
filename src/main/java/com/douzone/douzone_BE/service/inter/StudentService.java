package com.douzone.douzone_BE.service.inter;
import java.util.Map;

public interface StudentService {
    // 학색의 정보 return
    public Map<String, String> getStudentInfo(Long studentId);
    // 학생 정보 수정
    public boolean updateStudentInfo(Long studentId, Map<String, String> studentInfo);
}
