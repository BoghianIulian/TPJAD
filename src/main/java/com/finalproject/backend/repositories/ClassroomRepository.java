package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Classroom;
import com.finalproject.backend.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    boolean existsByHomeroomTeacher(Teacher teacher);

    Optional<Classroom> findByHomeroomTeacher(Teacher teacher);
    Optional<Classroom> findByHomeroomTeacherId(Long teacherId);

}
