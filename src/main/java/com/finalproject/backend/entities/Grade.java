package com.finalproject.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(
        name = "grades",
        indexes = {
                @Index(
                        name = "idx_grade_student_course_date",
                        columnList = "student_id, class_course_id, date"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

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
    private Integer value;

    @Column(nullable = false)
    private LocalDate date;
}

