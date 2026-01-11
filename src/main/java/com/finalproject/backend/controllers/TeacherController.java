package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.TeacherCreateDTO;
import com.finalproject.backend.dto.TeacherUpdateDTO;
import com.finalproject.backend.entities.Teacher;
import com.finalproject.backend.services.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Teacher> getAll() {
        return teacherService.getAll();
    }

    @GetMapping("/{id}")
    public Teacher getById(@PathVariable Long id) {
        return teacherService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Teacher create(@Valid @RequestBody TeacherCreateDTO dto) {
        return teacherService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")

    public Teacher update(@PathVariable Long id,
                          @Valid @RequestBody TeacherUpdateDTO dto) {
        return teacherService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        teacherService.delete(id);
    }

    @GetMapping("/generateRegCode")
    @PreAuthorize("hasRole('ADMIN')")
    public String generateRegCode()
    {
        return teacherService.generateUniqueRegistrationCode();
    }

}
