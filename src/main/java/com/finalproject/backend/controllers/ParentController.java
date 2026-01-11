package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.ParentCreateDTO;
import com.finalproject.backend.dto.ParentUpdateDTO;
import com.finalproject.backend.entities.Parent;
import com.finalproject.backend.services.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Parent> getAll() {
        return parentService.getAll();
    }

    @GetMapping("/{id}")
    public Parent getById(@PathVariable Long id) {
        return parentService.getById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Parent> getByStudent(@PathVariable Long studentId) {
        return parentService.getByStudent(studentId);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARENT', 'TEACHER', 'STUDENT')")
    public Parent getByUserId(@PathVariable Long userId) {
        return parentService.getByUserId(userId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Parent create(@Valid @RequestBody ParentCreateDTO dto) {
        return parentService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Parent update(@PathVariable Long id, @Valid @RequestBody ParentUpdateDTO dto) {
        return parentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        parentService.delete(id);
    }

    @GetMapping("/generateRegCode")
    @PreAuthorize("hasRole('ADMIN')")
    public String generateRegCode()
    {
         return parentService.generateUniqueRegistrationCode();
    }


}
