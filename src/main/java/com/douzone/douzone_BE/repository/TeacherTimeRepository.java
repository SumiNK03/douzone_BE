package com.douzone.douzone_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.douzone.douzone_BE.entity.TeacherTime;
import java.util.*;
import java.time.LocalTime;

@Repository
public interface TeacherTimeRepository extends JpaRepository<TeacherTime, Long> {
    List<TeacherTime> findByTeacher_Id(Long teacherId);
    List<TeacherTime> findByTeacher_IdAndDayOfWeek(Long teacherId, Integer dayOfWeek);
    List<TeacherTime> findByStatus(Integer status);
    List<TeacherTime> findByStudent_Id(Long studentId);
    List<TeacherTime> findByClazz_Id(Long clazzId);
    List<TeacherTime> findByDayOfWeekAndStartTime(Integer dayOfWeek, java.time.LocalTime startTime);
    List<TeacherTime> findByTeacher_IdAndStatus(Long teacherId, Integer status);
    List<TeacherTime> findByTeacher_IdAndClazz_Subject_Id(Long teacherId, Long subjectId);
    java.util.Optional<TeacherTime> findByTeacher_IdAndDayOfWeekAndStartTime(Long teacherId, Integer dayOfWeek, LocalTime startTime);
}