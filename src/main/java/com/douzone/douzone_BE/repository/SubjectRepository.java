package com.douzone.douzone_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.douzone.douzone_BE.entity.Subject;
import java.util.*;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findBySubjectName(String subjectName);
    java.util.Optional<Subject> findById(Long id);
}