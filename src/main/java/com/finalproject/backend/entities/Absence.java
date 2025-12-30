package com.finalproject.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(
        name = "absences",
        indexes = {
                @Index(
                        name = "idx_absence_student_course_date",
                        columnList = "student_id, class_course_id, date"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_course_id", nullable = false)
    private ClassCourse classCourse;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean excused = false;
}

