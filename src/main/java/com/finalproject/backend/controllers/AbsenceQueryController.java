package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.grade.ClassroomGradesResponse;
import com.finalproject.backend.dto.grade.CourseGradesDTO;
import com.finalproject.backend.dto.grade.CourseGradesResponse;
import com.finalproject.backend.dto.grade.StudentGradesResponse;
import com.finalproject.backend.entities.Absence;
import com.finalproject.backend.mappers.AbsenceGradeResponseMapper;
import com.finalproject.backend.services.AbsenceService;
import lombok.RequiredArgsConstructor;
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
    public CourseGradesResponse getByCourse(@PathVariable Long classCourseId) {
        List<Absence> absences = absenceService.getByClassCourse(classCourseId);
        return AbsenceGradeResponseMapper.toCourseGrades(
                List.of(),
                absences,
                true,
                false
        );
    }

    @GetMapping("/classroom/{classroomId}")
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