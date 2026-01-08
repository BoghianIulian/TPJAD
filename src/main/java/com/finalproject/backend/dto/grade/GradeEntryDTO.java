package com.finalproject.backend.dto.grade;

import java.time.LocalDate;

public record GradeEntryDTO(
        Long id,
        LocalDate date,
        Integer value
) {}

