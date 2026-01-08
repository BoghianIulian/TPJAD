package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.CreateGradeDTO;
import com.finalproject.backend.dto.UpdateGradeDTO;
import com.finalproject.backend.dto.bulk.BulkTestDTO;
import com.finalproject.backend.dto.grade.*;
import com.finalproject.backend.entities.Grade;
import com.finalproject.backend.mappers.GradeResponseMapper;
import com.finalproject.backend.services.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public StudentGradesResponse getByStudent(@PathVariable Long studentId) {
        return GradeResponseMapper.toStudentGrades(
                gradeService.getByStudent(studentId)
        );
    }

    @GetMapping("/student/{studentId}/course/{classCourseId}")
    public CourseGradesDTO getByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long classCourseId
    ) {
        return GradeResponseMapper.toStudentCourseGrades(
                gradeService.getByStudentAndClassCourse(studentId, classCourseId)
        );
    }

    @GetMapping("/course/{classCourseId}")
    public CourseGradesResponse getByCourse(@PathVariable Long classCourseId) {
        return GradeResponseMapper.toCourseGrades(
                gradeService.getByClassCourse(classCourseId)
        );
    }

    @GetMapping("/classroom/{classroomId}")
    public ClassroomGradesResponse getByClassroom(@PathVariable Long classroomId) {
        return GradeResponseMapper.toClassroomGrades(
                gradeService.getByClassroom(classroomId)
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

