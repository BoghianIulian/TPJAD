package com.finalproject.backend.dto.grade;

import java.util.List;

public record CourseStudentsGradesDTO(
        Long studentId,
        String studentName,
        String classroom,
        List<GradeEntryDTO> grades
) {}
