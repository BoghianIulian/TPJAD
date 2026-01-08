package com.finalproject.backend.dto.grade;

import java.util.List;

public record ClassroomStudentGradesDTO(
        Long studentId,
        String studentName,
        List<GradeEntryDTO> grades
) {}
