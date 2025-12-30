package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Grade;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /* ===================== STUDENT ===================== */

    // notes of a student at a specific subject
    List<Grade> findByStudentIdAndClassCourseIdOrderByDateAsc(
            Long studentId,
            Long classCourseId
    );

    // notes of a student at all subjects
    List<Grade> findByStudentIdOrderByDateAsc(
            Long studentId
    );

    /* ===================== TEACHER ===================== */

    // notes for a class at a specific subject
    List<Grade> findByClassCourseIdOrderByDateAsc(
            Long classCourseId
    );

    /* ===================== DIRIGINTE / CATALOG ===================== */

    @EntityGraph(attributePaths = {
            "student",
            "classCourse",
            "classCourse.course"
    })
    List<Grade> findByStudent_ClassroomIdOrderByDateAsc(
            Long classroomId
    );

    boolean existsByStudentIdAndClassCourseIdAndDate(
            Long studentId,
            Long classCourseId,
            LocalDate date
    );
}

