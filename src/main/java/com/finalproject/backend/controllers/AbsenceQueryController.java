package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.grade.ClassroomGradesResponse;
import com.finalproject.backend.dto.grade.CourseGradesDTO;
import com.finalproject.backend.dto.grade.CourseGradesResponse;
import com.finalproject.backend.dto.grade.StudentGradesResponse;
import com.finalproject.backend.entities.Absence;
import com.finalproject.backend.mappers.AbsenceGradeResponseMapper;
import com.finalproject.backend.services.AbsenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/absences")
@RequiredArgsConstructor
public class AbsenceQueryController {

    private final AbsenceService absenceService;

    /* ===================== GET ===================== */

    @GetMapping("/{id}")
    public Absence getById(@PathVariable Long id) {
        return absenceService.getById(id);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARENT','STUDENT')")
    public StudentGradesResponse getByStudent(@PathVariable Long studentId) {
        List<Absence> absences = absenceService.getByStudent(studentId);
        return AbsenceGradeResponseMapper.toStudentGrades(
                List.of(),
                absences,
                true,
                false
        );
    }

    @GetMapping("/student/{studentId}/course/{classCourseId}")
    public CourseGradesDTO getByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long classCourseId
    ) {
        List<Absence> absences =
                absenceService.getByStudentAndClassCourse(studentId, classCourseId);
        return AbsenceGradeResponseMapper.toStudentCourseGrades(
                List.of(),
                absences,
                true,
                false
        );
    }

    @GetMapping("/course/{classCourseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public CourseGradesResponse getByCourse(@PathVariable Long classCourseId) {
        return absenceService.getByCourseWithAllStudents(classCourseId);
    }

    @GetMapping("/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ClassroomGradesResponse getByClassroom(@PathVariable Long classroomId) {
        List<Absence> absences = absenceService.getByClassroom(classroomId);
        return AbsenceGradeResponseMapper.toClassroomGrades(
                List.of(),
                absences,
                true,
                false
        );
    }
}