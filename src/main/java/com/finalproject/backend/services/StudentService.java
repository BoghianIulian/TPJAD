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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    @Value("${PrefixStudent}")
    private  String REGISTRATION_CODE_PREFIX;

    @Value("${LETTERS}")
    private  String LETTERS;

    @Value("${DIGITS}")
    private  String DIGITS;

    private static SecureRandom RANDOM = new SecureRandom();

    private final StudentRepository studentRepo;
    private final ClassroomRepository classroomRepo;

    // CREATE
    public Student create(StudentCreateDTO dto) {


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

    public String generateUniqueRegistrationCode() {
        Set<String> existingCodes = new HashSet<>(studentRepo.getAllRegistrationCodes());
        String candidate;
        do {
            candidate = REGISTRATION_CODE_PREFIX + randomSuffix();
        } while (existingCodes.contains(candidate));
        return candidate;
    }

    private String randomSuffix() {
        return new StringBuilder()
                .append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())))
                .append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())))
                .append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())))
                .append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())))
                .toString();
    }
}
