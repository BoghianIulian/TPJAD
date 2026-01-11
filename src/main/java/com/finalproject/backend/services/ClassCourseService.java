package com.finalproject.backend.services;

import com.finalproject.backend.dto.ClassCourseDTO;
import com.finalproject.backend.entities.ClassCourse;
import com.finalproject.backend.entities.Classroom;
import com.finalproject.backend.entities.Course;
import com.finalproject.backend.entities.Teacher;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.ClassCourseRepository;
import com.finalproject.backend.repositories.ClassroomRepository;
import com.finalproject.backend.repositories.CourseRepository;
import com.finalproject.backend.repositories.TeacherRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassCourseService {

    private final ClassCourseRepository repo;
    private final ClassroomRepository classroomRepo;
    private final CourseRepository courseRepo;
    private final TeacherRepository teacherRepo;

    public ClassCourse create(ClassCourseDTO dto) {

        repo.findByClassroomIdAndCourseId(dto.getClassroomId(), dto.getCourseId())
                .ifPresent(x -> {
                    throw new EntityValidationException(
                            "This classroom already has this course assigned."
                    );
                });

        Classroom classroom = classroomRepo.findById(dto.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Teacher teacher = teacherRepo.findById(dto.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        ClassCourse cc = ClassCourse.builder()
                .classroom(classroom)
                .course(course)
                .teacher(teacher)
                .build();

        return repo.save(cc);
    }

    @Transactional(readOnly = true)
    public List<ClassCourse> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<ClassCourse> getByClassroomId(Long classroomId) {
        if (!classroomRepo.existsById(classroomId)) {
            throw new EntityNotFoundException("Classroom not found");
        }
        return repo.findByClassroomId(classroomId);
    }

    @Transactional(readOnly = true)
    public List<ClassCourse> getByTeacherId(Long teacherId) {
        if (!teacherRepo.existsById(teacherId)) {
            throw new EntityNotFoundException("Teacher not found");
        }
        return repo.findByTeacherId(teacherId);
    }


    @Transactional(readOnly = true)
    public ClassCourse getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ClassCourse not found"));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("ClassCourse not found");
        }
        repo.deleteById(id);
    }
}

