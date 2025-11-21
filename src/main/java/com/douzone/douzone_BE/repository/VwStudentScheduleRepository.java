package com.douzone.douzone_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.douzone.douzone_BE.entity.VwStudentSchedule;
import java.util.*;

@Repository
public interface VwStudentScheduleRepository extends JpaRepository<VwStudentSchedule, Long> {
    List<VwStudentSchedule> findByStudentId(Long studentId);
}
