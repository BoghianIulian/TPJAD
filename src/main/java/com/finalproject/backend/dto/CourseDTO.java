package com.finalproject.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseDTO {

    @NotBlank(message = "Course name is required")
    private String name;
}
