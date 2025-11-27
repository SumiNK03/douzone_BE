package com.douzone.douzone_BE.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.douzone.douzone_BE.service.inter.TeacherService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    // 강사 강의 일정
    // 날짜를 받아 하루 단위로 데이터 return
    @GetMapping("/schedule")
    public String teacherSchedule(@RequestParam String param) {
        return new String();
    }

    // 강사 정보를 return
    @GetMapping("/info")
    public ResponseEntity<?> teacherInfo(@RequestParam Long teacherId) {
        Map<String, String> teacherInfo = teacherService.getTeacherInfo(teacherId);

        if (teacherInfo != null) {
            return ResponseEntity.ok(teacherInfo);
        } else {
            return ResponseEntity.status(404).body("Teacher not found");
        }
    }

    
    
    // 강사 마이페이지 수정.
    @PostMapping("/edit")
    public ResponseEntity<?> teacherInfoEdit(@RequestBody Map<String, String> body) {
        Long teacherId = Long.parseLong(body.get("teacherId"));
        Map<String, String> teacherInfo = Map.of(
            "teacherName", body.get("teacherName"),
            "phone", body.get("phone")
        );
        boolean updateResult = teacherService.updateTeacherInfo(teacherId, teacherInfo);

        if (updateResult) {
            return ResponseEntity.ok("Teacher info updated successfully");
        } else {
            return ResponseEntity.status(404).body("Teacher not found");
        }
    }
    
}
