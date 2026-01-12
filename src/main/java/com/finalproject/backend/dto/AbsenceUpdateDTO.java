package com.finalproject.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.finalproject.backend.config.LocalDateDeserializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AbsenceUpdateDTO {

    @NotNull(message = "(date must not be null)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @NotNull(message = "(excused must not be null)")
    private Boolean excused;
}

