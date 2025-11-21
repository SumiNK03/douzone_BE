package com.douzone.douzone_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.douzone.douzone_BE.entity.Student;
import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByLoginId(String loginId);
    List<Student> findByStudentName(String studentName);
}
