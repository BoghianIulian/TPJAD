package com.finalproject.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.finalproject.backend.config.LocalDateDeserializer;
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
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "(must not be null)")
    private LocalDate date;
}
