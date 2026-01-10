package com.finalproject.backend.dto.absence;

import com.finalproject.backend.dto.grade.StudentInfoDTO;

import java.util.List;

public record StudentAbsencesResponse(
        StudentInfoDTO student,
        List<CourseAbsencesDTO> absencesByCourse
) {}