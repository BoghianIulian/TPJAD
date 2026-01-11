package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.grade.ClassroomGradesResponse;
import com.finalproject.backend.dto.grade.CourseGradesDTO;
import com.finalproject.backend.dto.grade.CourseGradesResponse;
import com.finalproject.backend.dto.grade.StudentGradesResponse;
import com.finalproject.backend.mappers.AbsenceGradeResponseMapper;
import com.finalproject.backend.services.AbsenceService;
import com.finalproject.backend.services.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/absence-grades")
@RequiredArgsConstructor
public class AbsenceGradeQueryController {

    private final GradeService gradeService;
    private final AbsenceService absenceService;

    /* ===================== GET ===================== */


    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARENT', 'STUDENT')")
    public StudentGradesResponse getByStudent(@PathVariable Long studentId) {
        return AbsenceGradeResponseMapper.toStudentGrades(
                gradeService.getByStudent(studentId),
                absenceService.getByStudent(studentId),
                true,
                true
        );
    }

    @GetMapping("/student/{studentId}/course/{classCourseId}")
    public CourseGradesDTO getByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long classCourseId
    ) {
        return AbsenceGradeResponseMapper.toStudentCourseGrades(
                gradeService.getByStudentAndClassCourse(studentId, classCourseId),
                absenceService.getByStudentAndClassCourse(studentId, classCourseId),
                true,
                true
        );
    }

    @GetMapping("/course/{classCourseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public CourseGradesResponse getByCourse(@PathVariable Long classCourseId) {
        return AbsenceGradeResponseMapper.toCourseGrades(
                gradeService.getByClassCourse(classCourseId),
                absenceService.getByClassCourse(classCourseId),
                true,
                true
        );
    }

    @GetMapping("/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ClassroomGradesResponse getByClassroom(@PathVariable Long classroomId) {
        return AbsenceGradeResponseMapper.toClassroomGrades(
                gradeService.getByClassroom(classroomId),
                absenceService.getByClassroom(classroomId),
                true,
                true
        );
    }
}
