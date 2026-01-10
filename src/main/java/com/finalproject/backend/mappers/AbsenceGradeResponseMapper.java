package com.finalproject.backend.mappers;

import com.finalproject.backend.dto.absence.AbsenceEntryDTO;
import com.finalproject.backend.dto.grade.*;
import com.finalproject.backend.entities.Absence;
import com.finalproject.backend.entities.Grade;

import java.util.*;

public class AbsenceGradeResponseMapper {

    private AbsenceGradeResponseMapper() {}

    /* ===================== STUDENT – ALL COURSES ===================== */

    public static StudentGradesResponse toStudentGrades(
            List<Grade> grades,
            List<Absence> absences,
            boolean includeAbsences,
            boolean includeGrades
    ) {
        validateFlags(includeAbsences, includeGrades);

        if (isEmpty(grades) && isEmpty(absences)) {
            return new StudentGradesResponse(null, List.of());
        }

        StudentInfoDTO student = resolveStudentInfo(grades, absences);

        Map<Long, CourseGradesDTO> grouped = new LinkedHashMap<>();

        if (includeGrades) {
            for (Grade g : safeList(grades)) {
                grouped.computeIfAbsent(
                        g.getClassCourse().getId(),
                        id -> new CourseGradesDTO(
                                id,
                                g.getClassCourse().getCourse().getName(),
                                g.getClassCourse().getTeacher().getLastName() + " " +
                                        g.getClassCourse().getTeacher().getFirstName(),
                                new ArrayList<>(),
                                new ArrayList<>()
                        )
                ).grades().add(
                        new GradeEntryDTO(g.getId(), g.getDate(), g.getValue())
                );
            }
        }

        if (includeAbsences) {
            for (Absence absence : safeList(absences)) {
                grouped.computeIfAbsent(
                        absence.getClassCourse().getId(),
                        id -> new CourseGradesDTO(
                                id,
                                absence.getClassCourse().getCourse().getName(),
                                absence.getClassCourse().getTeacher().getLastName() + " " +
                                        absence.getClassCourse().getTeacher().getFirstName(),
                                new ArrayList<>(),
                                new ArrayList<>()
                        )
                ).absences().add(
                        new AbsenceEntryDTO(
                                absence.getId(),
                                absence.getDate(),
                                absence.getExcused()
                        )
                );
            }
        }

        return new StudentGradesResponse(
                student,
                new ArrayList<>(grouped.values())
        );
    }

    /* ===================== STUDENT – ONE COURSE ===================== */

    public static CourseGradesDTO toStudentCourseGrades(
            List<Grade> grades,
            List<Absence> absences,
            boolean includeAbsences,
            boolean includeGrades
    ) {
        validateFlags(includeAbsences, includeGrades);

        if (isEmpty(grades) && isEmpty(absences)) {
            return null;
        }

        CourseContext context = resolveCourseContext(grades, absences);
        List<GradeEntryDTO> gradeEntries = includeGrades
                ? safeList(grades).stream()
                .map(g -> new GradeEntryDTO(g.getId(), g.getDate(), g.getValue()))
                .toList()
                : List.of();
        List<AbsenceEntryDTO> absenceEntries = includeAbsences
                ? safeList(absences).stream()
                .map(a -> new AbsenceEntryDTO(a.getId(), a.getDate(), a.getExcused()))
                .toList()
                : List.of();

        return new CourseGradesDTO(
                context.classCourseId(),
                context.courseName(),
                context.teacherName(),
                new ArrayList<>(gradeEntries),
                new ArrayList<>(absenceEntries)
        );
    }

    /* ===================== COURSE – ALL STUDENTS ===================== */

    public static CourseGradesResponse toCourseGrades(
            List<Grade> grades,
            List<Absence> absences,
            boolean includeAbsences,
            boolean includeGrades
    ) {
        validateFlags(includeAbsences, includeGrades);

        if (isEmpty(grades) && isEmpty(absences)) {
            return null;
        }

        CourseContext context = resolveCourseContext(grades, absences);

        Map<Long, CourseStudentsGradesDTO> students = new LinkedHashMap<>();

        if (includeGrades) {
            for (Grade g : safeList(grades)) {
                students.computeIfAbsent(
                        g.getStudent().getId(),
                        id -> new CourseStudentsGradesDTO(
                                id,
                                g.getStudent().getLastName() + " " + g.getStudent().getFirstName(),
                                g.getStudent().getClassroom().getName(),
                                new ArrayList<>(),
                                new ArrayList<>()
                        )
                ).grades().add(
                        new GradeEntryDTO(g.getId(), g.getDate(), g.getValue())
                );
            }
        }

        if (includeAbsences) {
            for (Absence absence : safeList(absences)) {
                students.computeIfAbsent(
                        absence.getStudent().getId(),
                        id -> new CourseStudentsGradesDTO(
                                id,
                                absence.getStudent().getLastName() + " " +
                                        absence.getStudent().getFirstName(),
                                absence.getStudent().getClassroom().getName(),
                                new ArrayList<>(),
                                new ArrayList<>()
                        )
                ).absences().add(
                        new AbsenceEntryDTO(
                                absence.getId(),
                                absence.getDate(),
                                absence.getExcused()
                        )
                );
            }
        }

        return new CourseGradesResponse(
                context.classCourseId(),
                context.courseName(),
                context.teacherName(),
                new ArrayList<>(students.values())
        );
    }

    /* ===================== CLASSROOM ===================== */

    public static ClassroomGradesResponse toClassroomGrades(
            List<Grade> grades,
            List<Absence> absences,
            boolean includeAbsences,
            boolean includeGrades
    ) {
        validateFlags(includeAbsences, includeGrades);

        if (isEmpty(grades) && isEmpty(absences)) {
            return null;
        }

        String classroom = resolveClassroom(grades, absences);

        Map<Long, ClassroomStudentGradesDTO> students = new LinkedHashMap<>();

        if (includeGrades) {
            for (Grade g : safeList(grades)) {
                students.computeIfAbsent(
                        g.getStudent().getId(),
                        id -> new ClassroomStudentGradesDTO(
                                id,
                                g.getStudent().getLastName() + " " + g.getStudent().getFirstName(),
                                new ArrayList<>(),
                                new ArrayList<>()
                        )
                ).grades().add(
                        new GradeEntryDTO(g.getId(), g.getDate(), g.getValue())
                );
            }
        }

        if (includeAbsences) {
            for (Absence absence : safeList(absences)) {
                students.computeIfAbsent(
                        absence.getStudent().getId(),
                        id -> new ClassroomStudentGradesDTO(
                                id,
                                absence.getStudent().getLastName() + " " +
                                        absence.getStudent().getFirstName(),
                                new ArrayList<>(),
                                new ArrayList<>()
                        )
                ).absences().add(
                        new AbsenceEntryDTO(
                                absence.getId(),
                                absence.getDate(),
                                absence.getExcused()
                        )
                );
            }
        }

        return new ClassroomGradesResponse(
                classroom,
                new ArrayList<>(students.values())
        );
    }

    private static void validateFlags(boolean includeAbsences, boolean includeGrades) {
        if (!includeAbsences && !includeGrades) {
            throw new IllegalArgumentException("At least one of absences or grades must be true");
        }
    }

    private static StudentInfoDTO resolveStudentInfo(
            List<Grade> grades,
            List<Absence> absences
    ) {
        if (!isEmpty(grades)) {
            Grade first = grades.get(0);
            return new StudentInfoDTO(
                    first.getStudent().getId(),
                    first.getStudent().getLastName() + " " + first.getStudent().getFirstName(),
                    first.getStudent().getClassroom().getName()
            );
        }

        Absence first = absences.get(0);
        return new StudentInfoDTO(
                first.getStudent().getId(),
                first.getStudent().getLastName() + " " + first.getStudent().getFirstName(),
                first.getStudent().getClassroom().getName()
        );
    }

    private static CourseContext resolveCourseContext(
            List<Grade> grades,
            List<Absence> absences
    ) {
        if (!isEmpty(grades)) {
            Grade first = grades.get(0);
            return new CourseContext(
                    first.getClassCourse().getId(),
                    first.getClassCourse().getCourse().getName(),
                    first.getClassCourse().getTeacher().getLastName() + " " +
                            first.getClassCourse().getTeacher().getFirstName()
            );
        }

        Absence first = absences.get(0);
        return new CourseContext(
                first.getClassCourse().getId(),
                first.getClassCourse().getCourse().getName(),
                first.getClassCourse().getTeacher().getLastName() + " " +
                        first.getClassCourse().getTeacher().getFirstName()
        );
    }

    private static String resolveClassroom(
            List<Grade> grades,
            List<Absence> absences
    ) {
        if (!isEmpty(grades)) {
            return grades.get(0).getStudent().getClassroom().getName();
        }
        return absences.get(0).getStudent().getClassroom().getName();
    }

    private static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    private static <T> List<T> safeList(List<T> list) {
        return list == null ? List.of() : list;
    }

    private record CourseContext(
            Long classCourseId,
            String courseName,
            String teacherName
    ) {}
}
