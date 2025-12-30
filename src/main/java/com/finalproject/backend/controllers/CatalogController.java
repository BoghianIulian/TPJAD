package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.catalog.CatalogStudentDTO;
import com.finalproject.backend.dto.catalog.ClassCatalogDTO;
import com.finalproject.backend.entities.Teacher;
import com.finalproject.backend.security.CustomUserDetails;
import com.finalproject.backend.services.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    /* ===================== DIRIGINTE ===================== */

    @GetMapping("/classroom/{classroomId}")
    public ClassCatalogDTO getClassCatalog(
            @PathVariable Long classroomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Teacher loggedTeacher = userDetails.getUser().getTeacher();
        return catalogService.getClassCatalog(classroomId, loggedTeacher);
    }

    /* ===================== PROFESOR ===================== */

    @GetMapping("/classroom/{classroomId}/course/{classCourseId}")
    public List<CatalogStudentDTO> getClassForCourse(
            @PathVariable Long classroomId,
            @PathVariable Long classCourseId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Teacher loggedTeacher = userDetails.getUser().getTeacher();
        return catalogService.getClassOverviewForTeacher(
                classroomId,
                classCourseId,
                loggedTeacher
        );
    }
}



