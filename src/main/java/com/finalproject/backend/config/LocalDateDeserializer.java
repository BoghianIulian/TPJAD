package com.finalproject.backend.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER_DD_MM_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FORMATTER_ISO = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        // Try dd-MM-yyyy format first
        try {
            return LocalDate.parse(dateString, FORMATTER_DD_MM_YYYY);
        } catch (DateTimeParseException e) {
            // If that fails, try ISO format (yyyy-MM-dd)
            try {
                return LocalDate.parse(dateString, FORMATTER_ISO);
            } catch (DateTimeParseException e2) {
                throw new IOException("Cannot deserialize LocalDate from: " + dateString + 
                        ". Expected format: dd-MM-yyyy or yyyy-MM-dd", e2);
            }
        }
    }
}
