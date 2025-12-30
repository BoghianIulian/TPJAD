package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.CreateGradeDTO;
import com.finalproject.backend.dto.UpdateGradeDTO;
import com.finalproject.backend.dto.bulk.BulkTestDTO;
import com.finalproject.backend.entities.Grade;
import com.finalproject.backend.services.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public List<Grade> getByStudent(@PathVariable Long studentId) {
        return gradeService.getByStudent(studentId);
    }

    @GetMapping("/student/{studentId}/course/{classCourseId}")
    public List<Grade> getByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long classCourseId
    ) {
        return gradeService.getByStudentAndClassCourse(studentId, classCourseId);
    }

    @GetMapping("/course/{classCourseId}")
    public List<Grade> getByCourse(@PathVariable Long classCourseId) {
        return gradeService.getByClassCourse(classCourseId);
    }

    @GetMapping("/classroom/{classroomId}")
    public List<Grade> getByClassroom(@PathVariable Long classroomId) {
        return gradeService.getByClassroom(classroomId);
    }

    /* ===================== CREATE ===================== */

    @PostMapping
    public Grade create(@RequestBody @Valid CreateGradeDTO dto) {
        return gradeService.create(
                dto.getStudentId(),
                dto.getClassCourseId(),
                dto.getDateGiven(),
                dto.getValue()
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

