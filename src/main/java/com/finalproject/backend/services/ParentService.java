package com.finalproject.backend.services;

import com.finalproject.backend.dto.ParentCreateDTO;
import com.finalproject.backend.dto.ParentUpdateDTO;
import com.finalproject.backend.entities.Parent;
import com.finalproject.backend.entities.Student;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.ParentRepository;
import com.finalproject.backend.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParentService {

    private final ParentRepository parentRepo;
    private final StudentRepository studentRepo;

    // CREATE
    public Parent create(ParentCreateDTO dto) {

        // registrationCode must be UNIQUE
        parentRepo.findByRegistrationCode(dto.getRegistrationCode())
                .ifPresent(p -> {
                    throw new EntityValidationException("Registration code already exists: " + dto.getRegistrationCode());
                });

        Student student = studentRepo.findById(dto.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Parent parent = Parent.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .registrationCode(dto.getRegistrationCode())
                .student(student)
                .build();

        return parentRepo.save(parent);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<Parent> getAll() {
        return parentRepo.findAll();
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public Parent getById(Long id) {
        return parentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));
    }

    // GET BY STUDENT
    @Transactional(readOnly = true)
    public List<Parent> getByStudent(Long studentId) {
        return parentRepo.findByStudentId(studentId);
    }

    // UPDATE
    public Parent update(Long id, ParentUpdateDTO dto) {

        Parent parent = parentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        Student student = studentRepo.findById(dto.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        parent.setFirstName(dto.getFirstName());
        parent.setLastName(dto.getLastName());
        parent.setStudent(student);

        return parentRepo.save(parent);
    }

    // DELETE
    public void delete(Long id) {
        if (!parentRepo.existsById(id)) {
            throw new EntityNotFoundException("Parent not found");
        }
        parentRepo.deleteById(id);
    }
}
