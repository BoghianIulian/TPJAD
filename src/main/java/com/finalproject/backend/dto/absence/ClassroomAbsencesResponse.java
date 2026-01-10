package com.finalproject.backend.dto.absence;

import java.util.List;

public record ClassroomAbsencesResponse(
        String classroom,
        List<ClassroomStudentAbsencesDTO> students
) {}