package com.douzone.douzone_BE.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/teacher")
public class TeacherController {

    // 강사 강의 일정
    // 날짜를 받아 하루 단위로 데이터 return
    @GetMapping("/schedule")
    public String teacherSchedule(@RequestParam String param) {
        return new String();
    }

    // 강사 정보를 return
    @GetMapping("/info")
    public String teacherInfo(@RequestParam String param) {
        return new String();
    }
    
    
    // 강사 마이페이지 수정.
    @PostMapping("/edit")
    public String teacherInfoEdit(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
