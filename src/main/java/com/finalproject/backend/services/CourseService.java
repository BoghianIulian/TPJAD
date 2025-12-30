package com.finalproject.backend.services;

import com.finalproject.backend.dto.CourseDTO;
import com.finalproject.backend.entities.Course;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.CourseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepo;

    public Course create(CourseDTO dto) {

        if (courseRepo.findByName(dto.getName()).isPresent()) {
            throw new EntityValidationException("Course already exists: " + dto.getName());
        }

        Course course = Course.builder()
                .name(dto.getName())
                .build();

        return courseRepo.save(course);
    }

    @Transactional(readOnly = true)
    public List<Course> getAll() {
        return courseRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Course getById(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

    public Course update(Long id, CourseDTO dto) {

        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        course.setName(dto.getName());
        return courseRepo.save(course);
    }

    public void delete(Long id) {
        if (!courseRepo.existsById(id)) {
            throw new EntityNotFoundException("Course not found");
        }
        courseRepo.deleteById(id);
    }
}
