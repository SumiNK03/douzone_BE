package com.douzone.douzone_BE.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 학생 스케줄 관리 컨트롤러

@RestController
@RequestMapping("/student")
public class StudentController {

    // 학생 강의 스케줄을 return.
    // id(쿠키)를 받아 정보 조회
    // 날짜, 시간 형태로 return
    @GetMapping("/schedule")
    public String studentSchedule(@RequestParam String param) {
        return new String();
    }
    
    // 학생 마이페이지 정보
    @GetMapping("/info")
    public String studentInfo(@RequestParam String param) {
        return new String();
    }
    
    // 학생 마이페이지 정보 수정
    @PostMapping("/edit")
    public String studentInfoEdit(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }

}
