package com.finalproject.backend.services;

import com.finalproject.backend.dto.TeacherCreateDTO;
import com.finalproject.backend.dto.TeacherUpdateDTO;
import com.finalproject.backend.entities.Teacher;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepo;

    public Teacher create(TeacherCreateDTO dto) {

        teacherRepo.findByRegistrationCode(dto.getRegistrationCode())
                .ifPresent(t -> {
                    throw new EntityValidationException("Registration code already exists: " + dto.getRegistrationCode());
                });

        Teacher t = Teacher.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .registrationCode(dto.getRegistrationCode())
                .build();

        return teacherRepo.save(t);
    }

    @Transactional(readOnly = true)
    public List<Teacher> getAll() {
        return teacherRepo.findAll();
    }
    @Transactional(readOnly = true)
    public Teacher getById(Long id) {
        return teacherRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));
    }

    public Teacher update(Long id, TeacherUpdateDTO dto) {

        Teacher t = teacherRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));

        t.setFirstName(dto.getFirstName());
        t.setLastName(dto.getLastName());

        return teacherRepo.save(t);
    }

    public void delete(Long id) {
        if (!teacherRepo.existsById(id)) {
            throw new EntityNotFoundException("Teacher not found: " + id);
        }
        teacherRepo.deleteById(id);
    }
}
