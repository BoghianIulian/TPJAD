package com.finalproject.backend.dto.grade;

import com.finalproject.backend.dto.absence.AbsenceEntryDTO;

import java.util.List;

public record CourseStudentsGradesDTO(
        Long studentId,
        String studentName,
        String classroom,
        List<GradeEntryDTO> grades,
        List<AbsenceEntryDTO> absences
) {}
