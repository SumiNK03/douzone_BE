package com.douzone.douzone_BE.service.impl;

import com.douzone.douzone_BE.service.inter.TeacherService;
import com.douzone.douzone_BE.repository.TeacherRepository;
import com.douzone.douzone_BE.entity.Teacher;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public Map<String, String> getTeacherInfo(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        if (teacher == null) {
            return null;
        }

        Map<String, String> teacherInfo = Map.of(
            "teacherName", teacher.getTeacherName(),
            "teacherId", teacher.getLoginId(),
            "phone", teacher.getPhone()
        );

        return teacherInfo;
    }

    @Override
    @Transactional
    public boolean updateTeacherInfo(Long teacherId, Map<String, String> teacherInfo) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        if (teacher == null) {
            return false;
        } else {
            teacher.setTeacherName(teacherInfo.get("teacherName"));
            teacher.setPhone(teacherInfo.get("phone"));
            teacherRepository.save(teacher);
            return true;
        }
    }

}
