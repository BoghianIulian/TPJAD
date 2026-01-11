package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.StudentCreateDTO;
import com.finalproject.backend.dto.StudentUpdateDTO;
import com.finalproject.backend.entities.Student;
import com.finalproject.backend.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Student create(@Valid @RequestBody StudentCreateDTO dto) {
        return studentService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Student update(@PathVariable Long id, @Valid @RequestBody StudentUpdateDTO dto) {
        return studentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    @GetMapping("/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<Student> getByClassroom(@PathVariable Long classroomId) {
        return studentService.getByClassroom(classroomId);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER', 'PARENT')")
    public Student getByUserId(@PathVariable Long userId) {
        return studentService.getByUserId(userId);
    }

    @GetMapping("/generateRegCode")
    @PreAuthorize("hasRole('ADMIN')")
    public String generateRegCode()
    {
        return studentService.generateUniqueRegistrationCode();
    }

}
