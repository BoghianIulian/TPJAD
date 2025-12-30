package com.finalproject.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "class_courses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"classroom_id", "course_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
}
