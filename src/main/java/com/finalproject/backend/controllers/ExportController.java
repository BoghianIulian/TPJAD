package com.finalproject.backend.controllers;

import com.finalproject.backend.entities.Classroom;
import com.finalproject.backend.entities.Parent;
import com.finalproject.backend.entities.Student;
import com.finalproject.backend.entities.Teacher;
import com.finalproject.backend.services.ClassroomService;
import com.finalproject.backend.services.ExcelExportService;
import com.finalproject.backend.services.ParentService;
import com.finalproject.backend.services.StudentService;
import com.finalproject.backend.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExcelExportService excelExportService;
    private final ParentService parentService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;

    /**
     * Export parents registration codes for a classroom (for homeroom teacher)
     */
    @GetMapping("/parents/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<byte[]> exportParentsByClassroom(@PathVariable Long classroomId) throws IOException {
        List<Parent> parents = parentService.getByClassroom(classroomId);
        ByteArrayOutputStream excelFile = excelExportService.exportParentsRegistrationCodes(parents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "parents_registration_codes.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile.toByteArray());
    }

    /**
     * Export students registration codes for a classroom (for homeroom teacher)
     */
    @GetMapping("/students/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<byte[]> exportStudentsByClassroom(@PathVariable Long classroomId) throws IOException {
        List<Student> students = studentService.getByClassroom(classroomId);
        ByteArrayOutputStream excelFile = excelExportService.exportStudentsRegistrationCodes(students);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "students_registration_codes.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile.toByteArray());
    }

    /**
     * Export teachers registration codes (for admin)
     */
    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportTeachers() throws IOException {
        List<Teacher> teachers = teacherService.getAll();
        ByteArrayOutputStream excelFile = excelExportService.exportTeachersRegistrationCodes(teachers);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Teachers.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile.toByteArray());
    }

    /**
     * Export parents registration codes for homeroom teacher's classroom
     */
    @GetMapping("/parents/homeroom-teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<byte[]> exportParentsByHomeroomTeacher(@PathVariable Long teacherId) throws IOException {
        Classroom classroom = classroomService.getByHomeroomTeacher(teacherId);
        List<Parent> parents = parentService.getByHomeroomTeacher(teacherId);
        ByteArrayOutputStream excelFile = excelExportService.exportParentsRegistrationCodes(parents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = "Parents " + classroom.getName() + ".xlsx";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile.toByteArray());
    }

    /**
     * Export students registration codes for homeroom teacher's classroom
     */
    @GetMapping("/students/homeroom-teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<byte[]> exportStudentsByHomeroomTeacher(@PathVariable Long teacherId) throws IOException {
        Classroom classroom = classroomService.getByHomeroomTeacher(teacherId);
        List<Student> students = studentService.getByClassroom(classroom.getId());
        ByteArrayOutputStream excelFile = excelExportService.exportStudentsRegistrationCodes(students);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = "Students " + classroom.getName() + ".xlsx";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile.toByteArray());
    }
}
