package com.finalproject.backend.mappers;


import com.finalproject.backend.dto.grade.*;
import com.finalproject.backend.entities.Grade;

import java.util.*;

public class GradeResponseMapper {

    private GradeResponseMapper() {}

    /* ===================== STUDENT – ALL COURSES ===================== */

    public static StudentGradesResponse toStudentGrades(List<Grade> grades) {

        if (grades == null || grades.isEmpty()) {
            return new StudentGradesResponse(null, List.of());
        }

        Grade first = grades.get(0);

        StudentInfoDTO student = new StudentInfoDTO(
                first.getStudent().getId(),
                first.getStudent().getLastName() + " " + first.getStudent().getFirstName(),
                first.getStudent().getClassroom().getName()
        );

        Map<Long, CourseGradesDTO> grouped = new LinkedHashMap<>();

        for (Grade g : grades) {
            grouped.computeIfAbsent(
                    g.getClassCourse().getId(),
                    id -> new CourseGradesDTO(
                            id,
                            g.getClassCourse().getCourse().getName(),
                            g.getClassCourse().getTeacher().getLastName() + " " +
                                    g.getClassCourse().getTeacher().getFirstName(),
                            new ArrayList<>()
                    )
            ).grades().add(
                    new GradeEntryDTO(g.getId(), g.getDate(), g.getValue())
            );
        }

        return new StudentGradesResponse(
                student,
                new ArrayList<>(grouped.values())
        );
    }

    /* ===================== STUDENT – ONE COURSE ===================== */

    public static CourseGradesDTO toStudentCourseGrades(List<Grade> grades) {

        if (grades == null || grades.isEmpty()) {
            return null;
        }

        Grade first = grades.get(0);

        List<GradeEntryDTO> entries = grades.stream()
                .map(g -> new GradeEntryDTO(g.getId(), g.getDate(), g.getValue()))
                .toList();

        return new CourseGradesDTO(
                first.getClassCourse().getId(),
                first.getClassCourse().getCourse().getName(),
                first.getClassCourse().getTeacher().getLastName() + " " +
                        first.getClassCourse().getTeacher().getFirstName(),
                entries
        );
    }

    /* ===================== COURSE – ALL STUDENTS ===================== */

    public static CourseGradesResponse toCourseGrades(List<Grade> grades) {

        if (grades == null || grades.isEmpty()) {
            return null;
        }

        Grade first = grades.get(0);

        Map<Long, CourseStudentsGradesDTO> students = new LinkedHashMap<>();

        for (Grade g : grades) {
            students.computeIfAbsent( // daca id ul studentului exista deja, se executa secventa de mai jos
                    g.getStudent().getId(),
                    id -> new CourseStudentsGradesDTO(
                            id,
                            g.getStudent().getLastName() + " " + g.getStudent().getFirstName(),
                            g.getStudent().getClassroom().getName(),
                            new ArrayList<>()
                    )
            ).grades().add( // aceasta secventa se executa de fiecare data
                    new GradeEntryDTO(g.getId(), g.getDate(), g.getValue())
            );
        }

        return new CourseGradesResponse(
                first.getClassCourse().getId(),
                first.getClassCourse().getCourse().getName(),
                first.getClassCourse().getTeacher().getLastName() + " " +
                        first.getClassCourse().getTeacher().getFirstName(),
                new ArrayList<>(students.values())
        );
    }

    /* ===================== CLASSROOM ===================== */

    public static ClassroomGradesResponse toClassroomGrades(List<Grade> grades) {

        if (grades == null || grades.isEmpty()) {
            return null;
        }

        String classroom = grades.get(0).getStudent().getClassroom().getName();

        Map<Long, ClassroomStudentGradesDTO> students = new LinkedHashMap<>();

        for (Grade g : grades) {
            students.computeIfAbsent(
                    g.getStudent().getId(),
                    id -> new ClassroomStudentGradesDTO(
                            id,
                            g.getStudent().getLastName() + " " + g.getStudent().getFirstName(),
                            new ArrayList<>()
                    )
            ).grades().add(
                    new GradeEntryDTO(g.getId(), g.getDate(), g.getValue())
            );
        }

        return new ClassroomGradesResponse(
                classroom,
                new ArrayList<>(students.values())
        );
    }
}
