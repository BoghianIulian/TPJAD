package com.finalproject.backend.services;

import com.finalproject.backend.dto.ClassroomDTO;
import com.finalproject.backend.entities.Classroom;
import com.finalproject.backend.entities.Teacher;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.ClassroomRepository;
import com.finalproject.backend.repositories.TeacherRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassroomService {

    private final ClassroomRepository classroomRepo;
    private final TeacherRepository teacherRepo;

    public Classroom create(ClassroomDTO dto) {

        Teacher teacher = teacherRepo.findById(dto.getHomeroomTeacherId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Teacher not found: " + dto.getHomeroomTeacherId()));


        if (classroomRepo.existsByHomeroomTeacher(teacher)) {
            throw new EntityValidationException("Teacher is already assigned as homeroom teacher to another class.");
        }

        Classroom classroom = Classroom.builder()
                .name(dto.getName())
                .homeroomTeacher(teacher)
                .build();

        return classroomRepo.save(classroom);
    }

    @Transactional(readOnly = true)
    public List<Classroom> getAll() {
        return classroomRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Classroom getById(Long id) {
        return classroomRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found: " + id));
    }

    public Classroom update(Long id, ClassroomDTO dto) {

        Classroom classroom = classroomRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found: " + id));

        Teacher teacher = teacherRepo.findById(dto.getHomeroomTeacherId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Teacher not found: " + dto.getHomeroomTeacherId()));


        classroomRepo.findByHomeroomTeacher(teacher).ifPresent(cl -> {
            if (!cl.getId().equals(id)) {
                throw new EntityValidationException("Teacher is already assigned to class: " + cl.getName());
            }
        });

        classroom.setName(dto.getName());
        classroom.setHomeroomTeacher(teacher);

        return classroomRepo.save(classroom);
    }

    public void delete(Long id) {
        if (!classroomRepo.existsById(id)) {
            throw new EntityNotFoundException("Classroom not found: " + id);
        }
        classroomRepo.deleteById(id);
    }
    @Transactional(readOnly = true)
    public Classroom getByHomeroomTeacher(Long teacherId) {

        return classroomRepo.findByHomeroomTeacherId(teacherId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No classroom found for teacher id: " + teacherId
                ));
    }

}
