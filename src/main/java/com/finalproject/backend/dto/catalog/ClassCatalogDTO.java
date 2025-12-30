package com.finalproject.backend.dto.catalog;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCatalogDTO {

    private Long classroomId;
    private String classroomName;

    // columns (same order for all students)
    private List<CatalogCourseDTO> courses;

    // rows (students ordered alphabetically)
    private List<CatalogStudentDTO> students;
}
