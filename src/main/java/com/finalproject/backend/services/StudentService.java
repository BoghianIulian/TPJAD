package com.finalproject.backend.services;

import com.finalproject.backend.dto.StudentCreateDTO;
import com.finalproject.backend.dto.StudentUpdateDTO;
import com.finalproject.backend.entities.Classroom;
import com.finalproject.backend.entities.Student;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.ClassroomRepository;
import com.finalproject.backend.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepo;
    private final ClassroomRepository classroomRepo;

    // CREATE
    public Student create(StudentCreateDTO dto) {

        if (studentRepo.findByRegistrationCode(dto.getRegistrationCode()).isPresent()) {
            throw new EntityValidationException("Registration code already exists: " + dto.getRegistrationCode());
        }

        Classroom classroom = classroomRepo.findById(dto.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));

        Student student = Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .classroom(classroom)
                .registrationCode(dto.getRegistrationCode())
                .build();

        return studentRepo.save(student);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<Student> getAll() {
        return studentRepo.findAll();
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public Student getById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    // UPDATE
    public Student update(Long id, StudentUpdateDTO dto) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Classroom classroom = classroomRepo.findById(dto.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setClassroom(classroom);

        return studentRepo.save(student);
    }

    // DELETE
    public void delete(Long id) {
        if (!studentRepo.existsById(id)) {
            throw new EntityNotFoundException("Student not found");
        }
        studentRepo.deleteById(id);
    }

    // GET STUDENTS BY CLASSROOM
    @Transactional(readOnly = true)
    public List<Student> getByClassroom(Long classroomId) {
        return studentRepo.findByClassroomId(classroomId);
    }
}
