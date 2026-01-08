package com.finalproject.backend.dto.grade;

import java.util.List;

public record ClassroomGradesResponse(
        String classroom,
        List<ClassroomStudentGradesDTO> students
) {}
