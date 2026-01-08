package com.finalproject.backend.dto.grade;

import java.util.List;

public record CourseGradesResponse(
        Long classCourseId,
        String course,
        String teacher,
        List<CourseStudentsGradesDTO> students
) {}

