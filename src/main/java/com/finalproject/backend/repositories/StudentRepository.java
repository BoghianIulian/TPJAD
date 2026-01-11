package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRegistrationCode(String registrationCode);

    @Query("select s.registrationCode from Student s")
    List<String> getAllRegistrationCodes();

    List<Student> findByClassroomId(Long classroomId);
    List<Student> findByClassroomIdOrderByLastNameAscFirstNameAsc(Long classroomId);

}
