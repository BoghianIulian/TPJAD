package com.finalproject.backend.services;

import com.finalproject.backend.entities.*;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final ClassCourseRepository classCourseRepository;
    private final ClassroomRepository classroomRepository;

    /* ===================== GET ===================== */

    @Transactional(readOnly = true)
    public Absence getById(Long id) {
        return absenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Absence not found"));
    }

    @Transactional(readOnly = true)
    public List<Absence> getByStudent(Long studentId) {
        requireStudent(studentId);
        return absenceRepository.findByStudentIdOrderByDateAsc(studentId);
    }

    @Transactional(readOnly = true)
    public List<Absence> getByStudentAndClassCourse(Long studentId, Long classCourseId) {
        requireStudent(studentId);
        requireClassCourse(classCourseId);
        return absenceRepository
                .findByStudentIdAndClassCourseIdOrderByDateAsc(studentId, classCourseId);
    }

    @Transactional(readOnly = true)
    public List<Absence> getByClassCourse(Long classCourseId) {
        requireClassCourse(classCourseId);
        return absenceRepository.findByClassCourseIdOrderByDateAsc(classCourseId);
    }

    @Transactional(readOnly = true)
    public List<Absence> getByClassroom(Long classroomId) {
        return absenceRepository.findByStudent_ClassroomIdOrderByDateAsc(classroomId);
    }

    /* ===================== CREATE ===================== */

    @Transactional
    public Absence create(Long studentId, Long classCourseId, LocalDate date) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        ClassCourse classCourse = classCourseRepository.findById(classCourseId)
                .orElseThrow(() -> new EntityNotFoundException("ClassCourse not found"));

        validateStudentClass(student, classCourse);

        return createAbsenceInternal(student, classCourse, date);
    }

    /* ===================== UPDATE ===================== */

    @Transactional
    public Absence update(Long absenceId, LocalDate newDate, Boolean excused) {

        Absence absence = absenceRepository.findById(absenceId)
                .orElseThrow(() -> new EntityNotFoundException("Absence not found"));

        if (!absence.getDate().equals(newDate)) {
            validateNoConflict(
                    absence.getStudent().getId(),
                    absence.getClassCourse().getId(),
                    newDate,
                    absenceId
            );
            absence.setDate(newDate);
        }

        absence.setExcused(excused);
        return absenceRepository.save(absence);
    }

    /* ===================== DELETE ===================== */

    @Transactional
    public void delete(Long id) {
        if (!absenceRepository.existsById(id)) {
            throw new EntityNotFoundException("Absence not found");
        }
        absenceRepository.deleteById(id);
    }

    /* ===================== INTERNAL ===================== */

    protected Absence createAbsenceInternal(
            Student student,
            ClassCourse classCourse,
            LocalDate date
    ) {
        validateNoConflict(student.getId(), classCourse.getId(), date, null);

        Absence absence = Absence.builder()
                .student(student)
                .classCourse(classCourse)
                .date(date)
                .excused(false)
                .build();

        return absenceRepository.save(absence);
    }

    private void validateStudentClass(Student student, ClassCourse classCourse) {
        if (!student.getClassroom().getId().equals(classCourse.getClassroom().getId())) {
            throw new EntityValidationException("Student does not belong to this classroom");
        }
    }

    private void requireStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student not found");
        }
    }

    private void requireClassCourse(Long classCourseId) {
        if (!classCourseRepository.existsById(classCourseId)) {
            throw new EntityNotFoundException("ClassCourse not found");
        }
    }

    private void requireClassroom(Long classroomId) {
        if (!classroomRepository.existsById(classroomId)) {
            throw new EntityNotFoundException("Classroom not found");
        }
    }

    private void validateNoConflict(
            Long studentId,
            Long classCourseId,
            LocalDate date,
            Long excludeAbsenceId
    ) {
        if (absenceRepository.existsByStudentIdAndClassCourseIdAndDate(
                studentId, classCourseId, date)) {

            if (excludeAbsenceId == null) {
                throw new EntityValidationException("Absence already exists for this date");
            }
        }

        if (gradeRepository.existsByStudentIdAndClassCourseIdAndDate(
                studentId, classCourseId, date)) {
            throw new EntityValidationException("Cannot add absence when grade exists for this date");
        }
    }
}


