package com.douzone.douzone_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.douzone.douzone_BE.entity.Class;
import java.util.*;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    List<Class> findByTeacher_Id(Long teacherId);
    List<Class> findBySubject_Id(Long subjectId);
    java.util.Optional<Class> findByTeacher_IdAndSubject_Id(Long teacherId, Long subjectId);
}
