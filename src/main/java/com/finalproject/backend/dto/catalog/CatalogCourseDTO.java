package com.finalproject.backend.dto.catalog;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogCourseDTO {

    private Long classCourseId;
    private String courseName;
    private String teacherName;
}
