package com.finalproject.backend.dto.grade;

import java.util.List;

public record StudentGradesResponse(
        StudentInfoDTO student,
        List<CourseGradesDTO> gradesByCourse
) {}


