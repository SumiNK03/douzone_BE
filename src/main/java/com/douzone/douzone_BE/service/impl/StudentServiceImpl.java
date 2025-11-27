package com.douzone.douzone_BE.service.impl;

import com.douzone.douzone_BE.repository.StudentRepository;
import com.douzone.douzone_BE.entity.Student;
import com.douzone.douzone_BE.service.inter.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public Map<String, String> getStudentInfo(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return null;
        } else {
            Map<String, String> studentInfo = Map.of(
                "studentName", student.getStudentName(),
                "studentId", student.getLoginId(),
                "phone", student.getPhone()
            );
            return studentInfo;
        }
    }

    @Override
    @Transactional
    public boolean updateStudentInfo(Long studentId, Map<String, String> studentInfo) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return false;
        } else {
            student.setStudentName(studentInfo.get("studentName"));
            student.setPhone(studentInfo.get("phone"));
            studentRepository.save(student);
            return true;
        }
    }
}
