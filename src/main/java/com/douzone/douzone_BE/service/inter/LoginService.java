package com.douzone.douzone_BE.service.inter;

import java.util.Map;

public interface  LoginService {

    boolean studentLogin(String studentId, String password);

    boolean teacherLogin(String teacherId, String password);

    boolean studentSignup(Map<String, String> studentInfo);

    boolean teacherSignup(Map<String, String> teacherInfo);

    boolean studentLeave(String studentId);

    boolean teacherLeave(String teacherId);
    
}
