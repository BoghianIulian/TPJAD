package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByRegistrationCode(String code);
}
