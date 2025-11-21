package com.douzone.douzone_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.douzone.douzone_BE.entity.Teacher;
import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByLoginId(String loginId);
    List<Teacher> findByTeacherName(String teacherName);
    java.util.Optional<Teacher> findById(Long id);
}
