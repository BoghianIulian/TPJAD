package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Absence;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {

    /* ===================== STUDENT ===================== */

    // absences of a student at a specific subject
    List<Absence> findByStudentIdAndClassCourseIdOrderByDateAsc(
            Long studentId,
            Long classCourseId
    );

    // absences of a student at all subjects
    List<Absence> findByStudentIdOrderByDateAsc(
            Long studentId
    );

    /* ===================== TEACHER ===================== */

    // absences for a class at a specific subject
    List<Absence> findByClassCourseIdOrderByDateAsc(
            Long classCourseId
    );

    /* ===================== DIRIGINTE / CATALOG ===================== */

    @EntityGraph(attributePaths = {
            "student",
            "classCourse",
            "classCourse.course"
    })
    List<Absence> findByStudent_ClassroomIdOrderByDateAsc(
            Long classroomId
    );

    boolean existsByStudentIdAndClassCourseIdAndDate(
            Long studentId,
            Long classCourseId,
            LocalDate date
    );
}

