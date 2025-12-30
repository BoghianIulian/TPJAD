package com.finalproject.backend.services;

import com.finalproject.backend.dto.bulk.BulkTestDTO;
import com.finalproject.backend.dto.bulk.StudentTestEntryDTO;
import com.finalproject.backend.entities.ClassCourse;
import com.finalproject.backend.entities.Grade;
import com.finalproject.backend.entities.Student;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.AbsenceRepository;
import com.finalproject.backend.repositories.ClassCourseRepository;
import com.finalproject.backend.repositories.GradeRepository;
import com.finalproject.backend.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final AbsenceRepository absenceRepository;
    private final StudentRepository studentRepository;
    private final ClassCourseRepository classCourseRepository;
    private final AbsenceService absenceService;

    /* ===================== GET ===================== */

    @Transactional(readOnly = true)
    public Grade getById(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found"));
    }

    @Transactional(readOnly = true)
    public List<Grade> getByStudent(Long studentId) {
        return gradeRepository.findByStudentIdOrderByDateAsc(studentId);
    }

    @Transactional(readOnly = true)
    public List<Grade> getByStudentAndClassCourse(Long studentId, Long classCourseId) {
        return gradeRepository
                .findByStudentIdAndClassCourseIdOrderByDateAsc(studentId, classCourseId);
    }

    @Transactional(readOnly = true)
    public List<Grade> getByClassCourse(Long classCourseId) {
        return gradeRepository.findByClassCourseIdOrderByDateAsc(classCourseId);
    }

    @Transactional(readOnly = true)
    public List<Grade> getByClassroom(Long classroomId) {
        return gradeRepository.findByStudent_ClassroomIdOrderByDateAsc(classroomId);
    }

    /* ===================== CREATE ===================== */

    @Transactional
    public Grade create(Long studentId, Long classCourseId, LocalDate date, Integer value) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        ClassCourse classCourse = classCourseRepository.findById(classCourseId)
                .orElseThrow(() -> new EntityNotFoundException("ClassCourse not found"));

        validateStudentClass(student, classCourse);

        return createGradeInternal(student, classCourse, date, value);
    }

    /* ===================== UPDATE ===================== */

    @Transactional
    public Grade update(Long gradeId, LocalDate newDate, Integer newValue) {

        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found"));

        if (!grade.getDate().equals(newDate)) {
            validateNoConflict(
                    grade.getStudent().getId(),
                    grade.getClassCourse().getId(),
                    newDate,
                    gradeId
            );
            grade.setDate(newDate);
        }

        grade.setValue(newValue);
        return gradeRepository.save(grade);
    }

    /* ===================== DELETE ===================== */

    @Transactional
    public void delete(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new EntityNotFoundException("Grade not found");
        }
        gradeRepository.deleteById(id);
    }

    /* ===================== BULK ===================== */

    @Transactional
    public void addBulkEvaluation(BulkTestDTO dto) {

        ClassCourse classCourse = classCourseRepository.findById(dto.getClassCourseId())
                .orElseThrow(() -> new EntityNotFoundException("ClassCourse not found"));

        if (!classCourse.getClassroom().getId().equals(dto.getClassroomId())) {
            throw new EntityValidationException("ClassCourse does not belong to classroom");
        }

        for (StudentTestEntryDTO entry : dto.getEntries()) {

            Student student = studentRepository.findById(entry.getStudentId())
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));

            if (!student.getClassroom().getId().equals(dto.getClassroomId())) {
                throw new EntityValidationException("Student does not belong to classroom");
            }

            if (entry.getGrade() != null && Boolean.TRUE.equals(entry.getAbsent())) {
                throw new EntityValidationException(
                        "Student cannot have both grade and absence"
                );
            }

            if (entry.getGrade() != null) {
                createGradeInternal(
                        student,
                        classCourse,
                        dto.getTestDate(),
                        entry.getGrade()
                );
            }

            if (Boolean.TRUE.equals(entry.getAbsent())) {
                absenceService.createAbsenceInternal(
                        student,
                        classCourse,
                        dto.getTestDate()
                );
            }
        }
    }

    /* ===================== INTERNAL ===================== */

    protected Grade createGradeInternal(
            Student student,
            ClassCourse classCourse,
            LocalDate date,
            Integer value
    ) {
        validateNoConflict(student.getId(), classCourse.getId(), date, null);

        Grade grade = Grade.builder()
                .student(student)
                .classCourse(classCourse)
                .date(date)
                .value(value)
                .build();

        return gradeRepository.save(grade);
    }

    private void validateStudentClass(Student student, ClassCourse classCourse) {
        if (!student.getClassroom().getId().equals(classCourse.getClassroom().getId())) {
            throw new EntityValidationException("Student does not belong to this classroom");
        }
    }

    private void validateNoConflict(
            Long studentId,
            Long classCourseId,
            LocalDate date,
            Long excludeGradeId
    ) {
        if (gradeRepository.existsByStudentIdAndClassCourseIdAndDate(
                studentId, classCourseId, date)) {

            if (excludeGradeId == null) {
                throw new EntityValidationException("Grade already exists for this date");
            }
        }

        if (absenceRepository.existsByStudentIdAndClassCourseIdAndDate(
                studentId, classCourseId, date)) {
            throw new EntityValidationException("Cannot add grade when absence exists for this date");
        }
    }
}


