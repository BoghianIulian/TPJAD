package com.finalproject.backend.dto.catalog;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogStudentDTO {

    private Long studentId;
    private String studentName;

    // same order as ClassCatalogDTO.courses
    private List<CatalogStudentCourseDTO> courses;
}
