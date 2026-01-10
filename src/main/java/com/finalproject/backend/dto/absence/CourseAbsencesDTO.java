package com.finalproject.backend.dto.absence;

import java.util.List;

public record CourseAbsencesDTO(
        Long classCourseId,
        String course,
        String teacher,
        List<AbsenceEntryDTO> absences
) {}