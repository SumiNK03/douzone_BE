package com.douzone.douzone_BE.service.impl;

import com.douzone.douzone_BE.service.inter.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.douzone.douzone_BE.repository.TeacherRepository;
import com.douzone.douzone_BE.repository.StudentRepository;
import com.douzone.douzone_BE.entity.Student;
import com.douzone.douzone_BE.entity.Teacher;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Override
    public boolean studentLogin(String studentId, String password) {
        Student student = studentRepository.findByLoginId(studentId);

        if (student != null && student.getLoginPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean teacherLogin(String teacherId, String password) {
        Teacher teacher = teacherRepository.findByLoginId(teacherId);

        if (teacher != null && teacher.getLoginPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean studentSignup(Map<String, String> studentInfo) {
        String loginId = studentInfo.get("loginId");
        String loginPassword = studentInfo.get("loginPassword");
        String studentName = studentInfo.get("studentName");
        String phone = studentInfo.get("phone");

        Student existingStudent = studentRepository.findByLoginId(loginId);
        if (existingStudent != null) {
            return false; // 이미 존재하는 아이디
        }

        Student newStudent = Student.builder()
                .loginId(loginId)
                .loginPassword(loginPassword)
                .studentName(studentName)
                .phone(phone)
                .build();

        studentRepository.save(newStudent);
        return true;
    }

    @Override
    @Transactional
    public boolean teacherSignup(Map<String, String> teacherInfo) {
        String loginId = teacherInfo.get("loginId");
        String loginPassword = teacherInfo.get("loginPassword");
        String teacherName = teacherInfo.get("teacherName");
        String phone = teacherInfo.get("phone");

        Teacher existingTeacher = teacherRepository.findByLoginId(loginId);
        if (existingTeacher != null) {
            return false; // 이미 존재하는 아이디
        }

        Teacher newTeacher = Teacher.builder()
                .loginId(loginId)
                .loginPassword(loginPassword)
                .teacherName(teacherName)
                .phone(phone)
                .build();

        teacherRepository.save(newTeacher);
        return true;
    }

    @Override
    @Transactional
    public boolean studentLeave(String studentId) {
        Student student = studentRepository.findById(Long.parseLong(studentId)).orElse(null);
        if (student != null) {
            studentRepository.delete(student);
            return true;
        } else {
            return false; // 학생을 찾을 수 없음
        }
    }

    @Override
    @Transactional
    public boolean teacherLeave(String teacherId) {
        Teacher teacher = teacherRepository.findById(Long.parseLong(teacherId)).orElse(null);
        if (teacher != null) {
            teacherRepository.delete(teacher);
            return true;
        } else {
            return false; // 교사를 찾을 수 없음
        }
    }

    
}
