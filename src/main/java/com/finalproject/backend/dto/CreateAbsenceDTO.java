package com.finalproject.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAbsenceDTO {

    @NotNull(message = "(must not be null)")
    private Long studentId;

    @NotNull(message = "(must not be null)")
    private Long classCourseId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "(must not be null)")
    private LocalDate date;
}
