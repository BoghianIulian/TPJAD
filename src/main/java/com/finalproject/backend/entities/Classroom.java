package com.finalproject.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "classrooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // example: "10A"

    @OneToMany(mappedBy = "classroom")
    @JsonManagedReference
    private List<Student> students;

    @OneToOne
    @JoinColumn(name = "homeroom_teacher_id", unique = true)
    @JsonBackReference
    private Teacher homeroomTeacher;
}
