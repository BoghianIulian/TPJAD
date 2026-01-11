package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.UpdateGradeDTO;
import com.finalproject.backend.dto.bulk.BulkTestDTO;
import com.finalproject.backend.dto.grade.*;
import com.finalproject.backend.entities.Grade;
import com.finalproject.backend.mappers.AbsenceGradeResponseMapper;
import com.finalproject.backend.services.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    /* ===================== GET ===================== */

    @GetMapping("/{id}")
    public Grade getById(@PathVariable Long id) {
        return gradeService.getById(id);
    }


    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARENT', 'STUDENT')")
    public StudentGradesResponse getByStudent(@PathVariable Long studentId) {
        return AbsenceGradeResponseMapper.toStudentGrades(
                gradeService.getByStudent(studentId),
                List.of(),
                false,
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
                List.of(),
                false,
                true
        );
    }

    @GetMapping("/course/{classCourseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public CourseGradesResponse getByCourse(@PathVariable Long classCourseId) {
        return AbsenceGradeResponseMapper.toCourseGrades(
                gradeService.getByClassCourse(classCourseId),
                List.of(),
                false,
                true
        );
    }

    @GetMapping("/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ClassroomGradesResponse getByClassroom(@PathVariable Long classroomId) {
        return AbsenceGradeResponseMapper.toClassroomGrades(
                gradeService.getByClassroom(classroomId),
                List.of(),
                false,
                true
        );
    }



    /* ===================== UPDATE ===================== */

    @PutMapping("/{id}")
    public Grade update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateGradeDTO dto
    ) {
        return gradeService.update(
                id,
                dto.getDate(),
                dto.getValue()
        );
    }

    /* ===================== DELETE ===================== */

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        gradeService.delete(id);
    }

    /* ===================== BULK (TEST / EVALUARE) ===================== */

    @PostMapping("/bulk")
    public void addBulkEvaluation(@RequestBody @Valid BulkTestDTO dto) {
        gradeService.addBulkEvaluation(dto);
    }
}

