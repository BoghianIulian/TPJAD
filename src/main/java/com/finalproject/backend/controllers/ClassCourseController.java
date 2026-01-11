package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.ClassCourseDTO;
import com.finalproject.backend.entities.ClassCourse;
import com.finalproject.backend.services.ClassCourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class-courses")
@RequiredArgsConstructor
public class ClassCourseController {

    private final ClassCourseService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<ClassCourse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ClassCourse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ClassCourse create(@Valid @RequestBody ClassCourseDTO dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<ClassCourse> getByClassroomId(@PathVariable Long classroomId) {
        return service.getByClassroomId(classroomId);
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<ClassCourse> getByTeacherId(@PathVariable Long teacherId) {
        return service.getByTeacherId(teacherId);
    }
}

