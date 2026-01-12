package com.finalproject.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.finalproject.backend.config.LocalDateDeserializer;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGradeDTO {

    @NotNull(message = "(must not be null)")
    private Long studentId;

    @NotNull(message = "(must not be null)")
    private Long classCourseId;

    @NotNull(message = "(must not be null)")
    @Min(value = 1, message = "(minimum allowed value is 1)")
    @Max(value = 10, message = "(maximum allowed value is 10)")
    private Integer value;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "(must not be null)")
    private LocalDate date;
}
