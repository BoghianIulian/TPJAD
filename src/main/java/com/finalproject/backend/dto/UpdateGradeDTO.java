package com.finalproject.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.finalproject.backend.config.LocalDateDeserializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGradeDTO {

    @Min(value = 1, message = "(minimum allowed value is 1)")
    @Max(value = 10, message = "(maximum allowed value is 10)")
    @NotNull(message = "value must not be null")
    private Integer value;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
}
