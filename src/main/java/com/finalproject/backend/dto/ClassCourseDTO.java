package com.finalproject.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassCourseDTO {

    @NotNull(message = "Classroom ID is required")
    private Long classroomId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
}

