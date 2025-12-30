package com.finalproject.backend.dto.catalog;

import com.finalproject.backend.dto.AbsenceDTO;
import com.finalproject.backend.dto.GradeDTO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogStudentCourseDTO {

    private Long classCourseId;

    private List<GradeDTO> grades;
    private List<AbsenceDTO> absences;
}
