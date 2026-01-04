package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.AbsenceUpdateDTO;
import com.finalproject.backend.dto.CreateAbsenceDTO;
import com.finalproject.backend.entities.Absence;
import com.finalproject.backend.services.AbsenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

