package com.finalproject.backend.services;

import com.finalproject.backend.dto.RegisterRequest;
import com.finalproject.backend.dto.RegisterWithCodeRequest;
import com.finalproject.backend.entities.Parent;
import com.finalproject.backend.entities.Student;
import com.finalproject.backend.entities.Teacher;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.enums.Role;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.ParentRepository;
import com.finalproject.backend.repositories.StudentRepository;
import com.finalproject.backend.repositories.TeacherRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User register(RegisterRequest dto) {

        // verificare dacă username există
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new EntityValidationException("Username already exists: " + dto.getUsername());
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();

        return userRepository.save(user);
    }
    @Transactional
    public User registerWithCode(RegisterWithCodeRequest dto)
    {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new EntityValidationException("Username already exists: " + dto.getUsername());
        }

        String code = dto.getRegistrationCode().trim().toUpperCase();
        Role role;
        Object targetEntity;

        // 3) deduce role + find entity by code + ensure it has no user

        if (code.startsWith("PROF-")) {
            role = Role.TEACHER;
            Teacher teacher = teacherRepository.findByRegistrationCode(code)
                    .orElseThrow(() -> new EntityValidationException("Invalid registration code (teacher not found): " + code));
            if (teacher.getUser() != null) {
                throw new EntityValidationException("Account already created for this teacher registration code: " + code);
            }
            targetEntity = teacher;

        } else if (code.startsWith("STUD-")) {
            role = Role.STUDENT;
            Student student = studentRepository.findByRegistrationCode(code)
                    .orElseThrow(() -> new EntityValidationException("Invalid registration code (student not found): " + code));
            if (student.getUser() != null) {
                throw new EntityValidationException("Account already created for this student registration code: " + code);
            }
            targetEntity = student;

        } else if (code.startsWith("PAR-")) {
            role = Role.PARENT;
            Parent parent = parentRepository.findByRegistrationCode(code)
                    .orElseThrow(() -> new EntityValidationException("Invalid registration code (parent not found): " + code));
            if (parent.getUser() != null) {
                throw new EntityValidationException("Account already created for this parent registration code: " + code);
            }
            targetEntity = parent;

        } else {
            throw new EntityValidationException("Invalid registration code prefix. Expected PROF-, STUD- or PAR-. Got: " + code);
        }

        // 4) create user
        User user = User.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        // 5) attach user to the found entity + save entity
        if (targetEntity instanceof Teacher teacher) {
            teacher.setUser(savedUser);
            teacherRepository.save(teacher);

        } else if (targetEntity instanceof Student student) {
            student.setUser(savedUser);
            studentRepository.save(student);

        } else if (targetEntity instanceof Parent parent) {
            parent.setUser(savedUser);
            parentRepository.save(parent);
        }

        return savedUser;


    }
}
