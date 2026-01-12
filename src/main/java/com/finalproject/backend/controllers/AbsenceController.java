package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.AbsenceUpdateDTO;
import com.finalproject.backend.dto.CreateAbsenceDTO;
import com.finalproject.backend.entities.Absence;
import com.finalproject.backend.services.AbsenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/absences")
@RequiredArgsConstructor
public class AbsenceController {

    private final AbsenceService absenceService;


    /* ===================== CREATE ===================== */

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Map<String, Object> create(@RequestBody @Valid CreateAbsenceDTO dto) {
        Absence a = absenceService.create(
                dto.getStudentId(),
                dto.getClassCourseId(),
                dto.getDate()
        );

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("id", a.getId());
        resp.put("studentLastName", a.getStudent().getLastName());
        resp.put("studentFirstName", a.getStudent().getFirstName());
        resp.put("courseName", a.getClassCourse().getCourse().getName());
        resp.put("date", a.getDate());       // LocalDate -> "yyyy-MM-dd"
        resp.put("excused", a.getExcused());

        return resp;
    }


    /* ===================== UPDATE ===================== */

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
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

    /* ===================== TOGGLE EXCUSED ===================== */

    @PatchMapping("/{id}/toggle-excused")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Map<String, Object> toggleExcused(@PathVariable Long id) {
        Absence a = absenceService.toggleExcused(id);

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("id", a.getId());
        resp.put("studentId", a.getStudent().getId());
        resp.put("studentLastName", a.getStudent().getLastName());
        resp.put("studentFirstName", a.getStudent().getFirstName());
        resp.put("classCourseId", a.getClassCourse().getId());
        resp.put("courseName", a.getClassCourse().getCourse().getName());
        resp.put("date", a.getDate()); // LocalDate -> "yyyy-MM-dd"
        resp.put("excused", a.getExcused());

        return resp;
    }

    /* ===================== DELETE ===================== */

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        absenceService.delete(id);
    }

    /* ===================== STATISTICS ===================== */

    @GetMapping("/student/{studentId}/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER', 'PARENT')")
    public Map<String, Object> getTotalAbsencesByStudent(@PathVariable Long studentId) {
        Long total = absenceService.getTotalAbsencesByStudent(studentId);
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("studentId", studentId);
        resp.put("totalAbsences", total);
        return resp;
    }

    @GetMapping("/student/{studentId}/unexcused")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER', 'PARENT')")
    public Map<String, Object> getUnexcusedAbsencesByStudent(@PathVariable Long studentId) {
        Long unexcused = absenceService.getUnexcusedAbsencesByStudent(studentId);
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("studentId", studentId);
        resp.put("unexcusedAbsences", unexcused);
        return resp;
    }
}

