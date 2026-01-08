package com.finalproject.backend.dto.grade;

import java.util.List;



public record CourseGradesDTO(
        Long classCourseId,
        String course,
        String teacher,
        List<GradeEntryDTO> grades
) {}


