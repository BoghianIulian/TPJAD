package com.finalproject.backend.dto.grade;

import com.finalproject.backend.dto.absence.AbsenceEntryDTO;

import java.util.List;



public record CourseGradesDTO(
        Long classCourseId,
        String course,
        String teacher,
        List<GradeEntryDTO> grades,
        List<AbsenceEntryDTO> absences
) {}


