package com.finalproject.backend.services;

import com.finalproject.backend.entities.Parent;
import com.finalproject.backend.entities.Student;
import com.finalproject.backend.entities.Teacher;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    /**
     * General method to create Excel file with registration codes
     * @param headers Array of header names
     * @param data List of rows, where each row is an array of strings
     * @return ByteArrayOutputStream containing the Excel file
     */
    public ByteArrayOutputStream createExcelFile(String[] headers, List<String[]> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Registration Codes");

        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(rowData[i]);
            }
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;
    }

    /**
     * Export parents registration codes for a classroom
     */
    public ByteArrayOutputStream exportParentsRegistrationCodes(List<Parent> parents) throws IOException {
        String[] headers = {"Parent Name", "Student Name", "Registration Code"};
        List<String[]> data = parents.stream()
                .map(parent -> new String[]{
                        parent.getFirstName() + " " + parent.getLastName(),
                        parent.getStudent().getFirstName() + " " + parent.getStudent().getLastName(),
                        parent.getRegistrationCode()
                })
                .toList();
        return createExcelFile(headers, data);
    }

    /**
     * Export students registration codes for a classroom
     */
    public ByteArrayOutputStream exportStudentsRegistrationCodes(List<Student> students) throws IOException {
        String[] headers = {"Student Name", "Registration Code"};
        List<String[]> data = students.stream()
                .map(student -> new String[]{
                        student.getFirstName() + " " + student.getLastName(),
                        student.getRegistrationCode()
                })
                .toList();
        return createExcelFile(headers, data);
    }

    /**
     * Export teachers registration codes
     */
    public ByteArrayOutputStream exportTeachersRegistrationCodes(List<Teacher> teachers) throws IOException {
        String[] headers = {"Teacher Name", "Registration Code"};
        List<String[]> data = teachers.stream()
                .map(teacher -> new String[]{
                        teacher.getFirstName() + " " + teacher.getLastName(),
                        teacher.getRegistrationCode()
                })
                .toList();
        return createExcelFile(headers, data);
    }
}
