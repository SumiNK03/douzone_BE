package com.douzone.douzone_BE.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.douzone.douzone_BE.service.inter.StudentService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

// 학생 스케줄 관리 컨트롤러

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    // 학생 강의 스케줄을 return.
    // id(쿠키)를 받아 정보 조회
    // 날짜, 시간 형태로 return
    @GetMapping("/schedule")
    public String studentSchedule(@RequestParam String param) {
        return new String();
    }
    
    // 학생 마이페이지 정보
    @GetMapping("/info")
    public ResponseEntity<?> studentInfo(@RequestParam Long studentId) {
        Map<String, String> studentInfo = studentService.getStudentInfo(studentId);

        if (studentInfo != null) {
            return ResponseEntity.ok(studentInfo);
        } else {
            return ResponseEntity.status(404).body("Student not found");
        }
    }
    
    // 학생 마이페이지 정보 수정
    @PostMapping("/edit")
    public ResponseEntity<?> studentInfoEdit(@RequestBody Map<String, String> body) {
        Long studentId = Long.parseLong(body.get("studentId"));
        Map<String, String> studentInfo = Map.of(
            "studentName", body.get("studentName"),
            "phone", body.get("phone")
        );
        boolean updateResult = studentService.updateStudentInfo(studentId, studentInfo);

        if (updateResult) {
            return ResponseEntity.ok("Student info updated successfully");
        } else {
            return ResponseEntity.status(404).body("Student not found");
        }
    }

}
