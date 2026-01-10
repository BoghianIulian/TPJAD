package com.finalproject.backend.dto.absence;

import java.util.List;

public record CourseStudentsAbsencesDTO(
        Long studentId,
        String studentName,
        String classroom,
        List<AbsenceEntryDTO> absences
) {}