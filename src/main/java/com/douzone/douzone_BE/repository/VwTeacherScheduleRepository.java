package com.douzone.douzone_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.douzone.douzone_BE.entity.VwTeacherSchedule;
import java.util.*;

@Repository
public interface VwTeacherScheduleRepository extends JpaRepository<VwTeacherSchedule, Long> {
    List<VwTeacherSchedule> findByTeacherId(Long teacherId);
}
