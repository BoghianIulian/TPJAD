package com.finalproject.backend.dto.grade;

import com.finalproject.backend.dto.absence.AbsenceEntryDTO;

import java.util.List;

public record ClassroomStudentGradesDTO(
        Long studentId,
        String studentName,
        List<GradeEntryDTO> grades,
        List<AbsenceEntryDTO> absences
) {}
