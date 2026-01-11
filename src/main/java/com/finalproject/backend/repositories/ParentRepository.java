package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    Optional<Parent> findByRegistrationCode(String code);
    @Query("select p.registrationCode from Parent p")
    List<String> getAllRegistrationCodes();

    List<Parent> findByStudentId(Long studentId);

    @Query("SELECT p FROM Parent p WHERE p.user.id = :userId")
    Optional<Parent> findByUserId(@Param("userId") Long userId);
}
