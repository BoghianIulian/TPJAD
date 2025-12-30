package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.AbsenceUpdateDTO;
import com.finalproject.backend.dto.CreateAbsenceDTO;
import com.finalproject.backend.entities.Absence;
import com.finalproject.backend.services.AbsenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/absences")
@RequiredArgsConstructor
public class AbsenceController {

    private final AbsenceService absenceService;

    /* ===================== GET ===================== */

    @GetMapping("/{id}")
    public Absence getById(@PathVariable Long id) {
        return absenceService.getById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Absence> getByStudent(@PathVariable Long studentId) {
        return absenceService.getByStudent(studentId);
    }

    @GetMapping("/student/{studentId}/course/{classCourseId}")
    public List<Absence> getByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long classCourseId
    ) {
        return absenceService.getByStudentAndClassCourse(studentId, classCourseId);
    }

    @GetMapping("/course/{classCourseId}")
    public List<Absence> getByCourse(@PathVariable Long classCourseId) {
        return absenceService.getByClassCourse(classCourseId);
    }

    @GetMapping("/classroom/{classroomId}")
    public List<Absence> getByClassroom(@PathVariable Long classroomId) {
        return absenceService.getByClassroom(classroomId);
    }

    /* ===================== CREATE ===================== */

    @PostMapping
    public Absence create(@RequestBody @Valid CreateAbsenceDTO dto) {
        return absenceService.create(
                dto.getStudentId(),
                dto.getClassCourseId(),
                dto.getDate()
        );
    }

    /* ===================== UPDATE ===================== */

    @PutMapping("/{id}")
    public Absence update(
            @PathVariable Long id,
            @RequestBody @Valid AbsenceUpdateDTO dto
    ) {
        return absenceService.update(
                id,
                dto.getDate(),
                dto.getExcused()
        );
    }

    /* ===================== DELETE ===================== */

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        absenceService.delete(id);
    }
}

