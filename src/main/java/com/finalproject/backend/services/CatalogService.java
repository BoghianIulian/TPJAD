package com.finalproject.backend.services;

import com.finalproject.backend.dto.AbsenceDTO;
import com.finalproject.backend.dto.GradeDTO;
import com.finalproject.backend.dto.catalog.*;
import com.finalproject.backend.entities.*;
import com.finalproject.backend.exceptions.EntityNotFoundException;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogService {

    private final ClassroomRepository classroomRepository;
    private final StudentRepository studentRepository;
    private final ClassCourseRepository classCourseRepository;
    private final GradeRepository gradeRepository;
    private final AbsenceRepository absenceRepository;



    public ClassCatalogDTO getClassCatalog(Long classroomId, Teacher loggedTeacher) {

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));

        if (classroom.getHomeroomTeacher() == null ||
                !classroom.getHomeroomTeacher().getId().equals(loggedTeacher.getId())) {
            throw new EntityValidationException("You are not the homeroom teacher of this class");
        }

        /* ===================== LOAD DATA ===================== */

        List<Student> students =
                studentRepository.findByClassroomIdOrderByLastNameAscFirstNameAsc(classroomId);

        List<ClassCourse> classCourses =
                classCourseRepository.findByClassroomIdOrderByCourse_NameAsc(classroomId);

        List<Grade> grades =
                gradeRepository.findByStudent_ClassroomIdOrderByDateAsc(classroomId);

        List<Absence> absences =
                absenceRepository.findByStudent_ClassroomIdOrderByDateAsc(classroomId);

        /* ===================== COURSE HEADER ===================== */

        Map<Long, Integer> courseIndexMap = new HashMap<>();
        List<CatalogCourseDTO> courseDTOs = new ArrayList<>();

        for (int i = 0; i < classCourses.size(); i++) {
            ClassCourse cc = classCourses.get(i);
            courseIndexMap.put(cc.getId(), i);

            courseDTOs.add(
                    CatalogCourseDTO.builder()
                            .classCourseId(cc.getId())
                            .courseName(cc.getCourse().getName())
                            .teacherName(
                                    cc.getTeacher().getLastName() + " " +
                                            cc.getTeacher().getFirstName()
                            )
                            .build()
            );
        }

        //STUDENT ROWS

        Map<Long, CatalogStudentDTO> studentMap = new LinkedHashMap<>();

        for (Student student : students) {

            List<CatalogStudentCourseDTO> cells = new ArrayList<>();

            for (ClassCourse cc : classCourses) {
                cells.add(
                        CatalogStudentCourseDTO.builder()
                                .classCourseId(cc.getId())
                                .grades(new ArrayList<>())
                                .absences(new ArrayList<>())
                                .build()
                );
            }

            studentMap.put(
                    student.getId(),
                    CatalogStudentDTO.builder()
                            .studentId(student.getId())
                            .studentName(
                                    student.getLastName() + " " +
                                            student.getFirstName()
                            )
                            .courses(cells)
                            .build()
            );
        }

        //POPULATE GRADES

        for (Grade grade : grades) {
            CatalogStudentDTO studentDTO =
                    studentMap.get(grade.getStudent().getId());

            int courseIdx =
                    courseIndexMap.get(grade.getClassCourse().getId());

            studentDTO.getCourses().get(courseIdx).getGrades()
                    .add(new GradeDTO(grade.getValue(), grade.getDate()));
        }

        //*POPULATE ABSENCES

        for (Absence absence : absences) {
            CatalogStudentDTO studentDTO =
                    studentMap.get(absence.getStudent().getId());

            int courseIdx =
                    courseIndexMap.get(absence.getClassCourse().getId());

            studentDTO.getCourses().get(courseIdx).getAbsences()
                    .add(new AbsenceDTO(absence.getDate(), absence.getExcused()));
        }

        /* ===================== RESULT ===================== */

        return ClassCatalogDTO.builder()
                .classroomId(classroom.getId())
                .classroomName(classroom.getName())
                .courses(courseDTOs)
                .students(new ArrayList<>(studentMap.values()))
                .build();
    }



    public List<CatalogStudentDTO> getClassOverviewForTeacher(
            Long classroomId,
            Long classCourseId,
            Teacher loggedTeacher
    ) {

        ClassCourse classCourse = classCourseRepository.findById(classCourseId)
                .orElseThrow(() -> new EntityNotFoundException("ClassCourse not found"));

        if (!classCourse.getClassroom().getId().equals(classroomId)) {
            throw new EntityValidationException("Course does not belong to this classroom");
        }

        if (!classCourse.getTeacher().getId().equals(loggedTeacher.getId())) {
            throw new EntityValidationException("You are not the teacher of this course");
        }

        List<Student> students =
                studentRepository.findByClassroomIdOrderByLastNameAscFirstNameAsc(classroomId);

        List<Grade> grades =
                gradeRepository.findByClassCourseIdOrderByDateAsc(classCourseId);

        List<Absence> absences =
                absenceRepository.findByClassCourseIdOrderByDateAsc(classCourseId);

        Map<Long, CatalogStudentDTO> studentMap = new LinkedHashMap<>();

        for (Student student : students) {
            studentMap.put(
                    student.getId(),
                    CatalogStudentDTO.builder()
                            .studentId(student.getId())
                            .studentName(
                                    student.getLastName() + " " +
                                            student.getFirstName()
                            )
                            .courses(List.of(
                                    CatalogStudentCourseDTO.builder()
                                            .classCourseId(classCourseId)
                                            .grades(new ArrayList<>())
                                            .absences(new ArrayList<>())
                                            .build()
                            ))
                            .build()
            );
        }

        for (Grade grade : grades) {
            studentMap.get(grade.getStudent().getId())
                    .getCourses().get(0)
                    .getGrades()
                    .add(new GradeDTO(grade.getValue(), grade.getDate()));
        }

        for (Absence absence : absences) {
            studentMap.get(absence.getStudent().getId())
                    .getCourses().get(0)
                    .getAbsences()
                    .add(new AbsenceDTO(absence.getDate(), absence.getExcused()));
        }

        return new ArrayList<>(studentMap.values());
    }
}
