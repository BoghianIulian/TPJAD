package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByRegistrationCode(String code);

    @Query("select t.registrationCode from Teacher t")
    List<String> getAllRegistrationCodes();
}
