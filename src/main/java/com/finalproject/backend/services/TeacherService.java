package com.finalproject.backend.services;

import com.finalproject.backend.dto.TeacherCreateDTO;
import com.finalproject.backend.dto.TeacherUpdateDTO;
import com.finalproject.backend.entities.Teacher;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.TeacherRepository;
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
public class TeacherService {

    @Value("${PrefixTeacher}")
    private static String REGISTRATION_CODE_PREFIX;

    @Value("${LETTERS}")
    private static String LETTERS;

    @Value("${DIGITS}")
    private static String DIGITS;

    private static final SecureRandom RANDOM = new SecureRandom();

    private final TeacherRepository teacherRepo;

    public Teacher create(TeacherCreateDTO dto) {




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

    public String generateUniqueRegistrationCode() {
        Set<String> existingCodes = new HashSet<>(teacherRepo.getAllRegistrationCodes());
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
