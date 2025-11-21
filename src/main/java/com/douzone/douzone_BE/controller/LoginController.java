package com.douzone.douzone_BE.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.douzone.douzone_BE.service.inter.LoginService;
import com.douzone.douzone_BE.repository.TeacherRepository;
import com.douzone.douzone_BE.repository.StudentRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.douzone.douzone_BE.entity.Student;


@RestController
@RequestMapping("/login")
@Tag(name = "로그인 API", description = "로그인 및 회원가입")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    // 인코딩
    private String safeEncode(String value) {
        if (value == null) return "";
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return value.replace(" ", "%20");
        }
    }

    @PostMapping("/studentLogin")
    public ResponseEntity<?> studentLogin(@RequestBody Map<String, String> body, HttpServletResponse response) {
        String studentId = body.get("studentId");
        String password = body.get("password");
        boolean loginResult = loginService.studentLogin(studentId, password);

        if (loginResult) {
            Student student = studentRepository.findByLoginId(studentId);
            Cookie idCookie = new Cookie("studentId", safeEncode(student.getId().toString()));
            idCookie.setPath("/");
            Cookie roleCookie = new Cookie("role", "student");
            roleCookie.setPath("/");
            String studentName = studentRepository.findByLoginId(studentId).getStudentName();
            Cookie nameCookie = new Cookie("name", safeEncode(studentName));
            nameCookie.setPath("/");

            response.addCookie(idCookie);
            response.addCookie(roleCookie);
            response.addCookie(nameCookie);

            return ResponseEntity.ok("Student login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid student ID or password");
        }
    }

    @PostMapping("/teacherLogin")
    public ResponseEntity<?> teacherLogin(@RequestBody Map<String, String> body, HttpServletResponse response) {
        String teacherId = body.get("teacherId");
        String password = body.get("password");
        boolean loginResult = loginService.teacherLogin(teacherId, password);

        if (loginResult) {
            Cookie idCookie = new Cookie("teacherId", safeEncode(teacherId));
            idCookie.setPath("/");
            Cookie roleCookie = new Cookie("role", "teacher");
            roleCookie.setPath("/");
            String teacherName = teacherRepository.findByLoginId(teacherId).getTeacherName();
            Cookie nameCookie = new Cookie("name", safeEncode(teacherName));
            nameCookie.setPath("/");

            response.addCookie(idCookie);
            response.addCookie(roleCookie);
            response.addCookie(nameCookie);

            return ResponseEntity.ok("Teacher login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid teacher ID or password");
        }
    }

    @PostMapping("/studentSignup")
    public ResponseEntity<?> studentSignup(@RequestBody Map<String, String> body) {
        boolean signupResult = loginService.studentSignup(body);

        if (signupResult) {
            return ResponseEntity.ok("Student signup successful");
        } else {
            return ResponseEntity.status(409).body("Student ID already exists");
        }
    }

    @PostMapping("/teacherSignup")
    public ResponseEntity<?> teacherSignup(@RequestBody Map<String, String> body) {
        boolean signupResult = loginService.teacherSignup(body);

        if (signupResult) {
            return ResponseEntity.ok("Teacher signup successful");
        } else {
            return ResponseEntity.status(409).body("Teacher ID already exists");
        }
    }
    
}
