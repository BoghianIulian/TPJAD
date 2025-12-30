package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.ClassCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassCourseRepository extends JpaRepository<ClassCourse, Long> {

    Optional<ClassCourse> findByClassroomIdAndCourseId(Long classroomId, Long courseId);

    List<ClassCourse> findByClassroomId(Long classroomId);

    List<ClassCourse> findByTeacherId(Long teacherId);
    List<ClassCourse> findByClassroomIdOrderByCourse_NameAsc(Long classroomId);

}

