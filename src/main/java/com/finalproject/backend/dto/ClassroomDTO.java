package com.finalproject.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassroomDTO {

    @NotBlank(message = "Class name is required") // (ex: 10A, 9B)
    private String name;

    @NotNull(message = "Homeroom teacher ID is required") // (ID-ul dirigintelui)
    private Long homeroomTeacherId;
}
