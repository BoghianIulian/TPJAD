package com.finalproject.backend.dto.absence;

import java.util.List;

public record ClassroomStudentAbsencesDTO(
        Long studentId,
        String studentName,
        List<AbsenceEntryDTO> absences
) {}