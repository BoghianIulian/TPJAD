package com.finalproject.backend.dto.absence;

import java.util.List;

public record CourseAbsencesResponse(
        Long classCourseId,
        String course,
        String teacher,
        List<CourseStudentsAbsencesDTO> students
) {}