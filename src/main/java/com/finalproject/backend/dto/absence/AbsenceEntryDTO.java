package com.finalproject.backend.dto.absence;

import java.time.LocalDate;

public record AbsenceEntryDTO(
        Long id,
        LocalDate date,
        Boolean excused
) {}
