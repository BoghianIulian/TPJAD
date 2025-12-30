package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.ClassroomDTO;
import com.finalproject.backend.entities.Classroom;
import com.finalproject.backend.services.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    // GET all classrooms (ADMIN only)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<Classroom> getAll() {
        return classroomService.getAll();
    }

    // GET classroom by id (available to all authenticated users)
    @GetMapping("/{id}")
    public Classroom getById(@PathVariable Long id) {
        return classroomService.getById(id);
    }

    // CREATE classroom (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Classroom create(@Valid @RequestBody ClassroomDTO dto) {
        return classroomService.create(dto);
    }

    // UPDATE classroom (ADMIN only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Classroom update(@PathVariable Long id,
                            @Valid @RequestBody ClassroomDTO dto) {
        return classroomService.update(id, dto);
    }

    // DELETE classroom (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        classroomService.delete(id);
    }
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Classroom getByHomeroomTeacher(@PathVariable Long teacherId) {
        return classroomService.getByHomeroomTeacher(teacherId);
    }

}
