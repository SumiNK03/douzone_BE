package com.douzone.douzone_BE.service;

import com.douzone.douzone_BE.entity.Class;
import com.douzone.douzone_BE.entity.Student;
import com.douzone.douzone_BE.entity.Teacher;
import com.douzone.douzone_BE.entity.TeacherTime;
import com.douzone.douzone_BE.entity.Subject;
import com.douzone.douzone_BE.repository.*;
import com.douzone.douzone_BE.service.inter.EnrollService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EnrollIntegrationTest {

    @Autowired
    private EnrollService enrollService;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private TeacherTimeRepository teacherTimeRepository;

    @Test
    @Transactional
    public void studentCanEnrollForMonWedFri() {
        // 준비: 강사, 과목, 분반, 학생, 그리고 teacher_time(월/수/금) 생성
        Teacher teacher = Teacher.builder()
                .loginId("t_login")
                .loginPassword("pass")
                .teacherName("TestTeacher")
                .phone("010-0000-0000")
                .build();
        teacher = teacherRepository.save(teacher);

        Subject subject = Subject.builder()
                .subjectName("TestSubject")
                .build();
        subject = subjectRepository.save(subject);

        Class clazz = Class.builder()
                .teacher(teacher)
                .subject(subject)
                .build();
        clazz = classRepository.save(clazz);

        Student student = Student.builder()
                .loginId("s_login")
                .loginPassword("pass")
                .studentName("TestStudent")
                .phone("010-1111-1111")
                .build();
        student = studentRepository.save(student);

        // 월(1), 수(3), 금(5) 13:00에 공강(status=0)으로 생성
        TeacherTime t1 = TeacherTime.builder()
                .teacher(teacher)
                .dayOfWeek(1)
                .startTime(LocalTime.of(13,0))
                .status(0)
                .clazz(clazz)
                .student(null)
                .build();

        TeacherTime t2 = TeacherTime.builder()
                .teacher(teacher)
                .dayOfWeek(3)
                .startTime(LocalTime.of(13,0))
                .status(0)
                .clazz(clazz)
                .student(null)
                .build();

        TeacherTime t3 = TeacherTime.builder()
                .teacher(teacher)
                .dayOfWeek(5)
                .startTime(LocalTime.of(13,0))
                .status(0)
                .clazz(clazz)
                .student(null)
                .build();

        t1 = teacherTimeRepository.save(t1);
        t2 = teacherTimeRepository.save(t2);
        t3 = teacherTimeRepository.save(t3);

        // 실행: 수강신청 (월수금)
        List<Integer> days = Arrays.asList(1,3,5);
        LocalTime time = LocalTime.of(13, 0); // 13:00
        enrollService.enrollClass(student.getId(), teacher.getId(), subject.getId(), days, time);

        // 검증: 각 시간의 status가 1로 변경되고 student, clazz 필드가 설정되어 있어야 함
        TeacherTime after1 = teacherTimeRepository.findById(t1.getId()).orElseThrow();
        TeacherTime after2 = teacherTimeRepository.findById(t2.getId()).orElseThrow();
        TeacherTime after3 = teacherTimeRepository.findById(t3.getId()).orElseThrow();

        assertThat(after1.getStatus()).isEqualTo(1);
        assertThat(after2.getStatus()).isEqualTo(1);
        assertThat(after3.getStatus()).isEqualTo(1);

        assertThat(after1.getStudent()).isNotNull();
        assertThat(after1.getStudent().getId()).isEqualTo(student.getId());

        assertThat(after2.getStudent()).isNotNull();
        assertThat(after2.getStudent().getId()).isEqualTo(student.getId());

        assertThat(after3.getStudent()).isNotNull();
        assertThat(after3.getStudent().getId()).isEqualTo(student.getId());
    }
}
