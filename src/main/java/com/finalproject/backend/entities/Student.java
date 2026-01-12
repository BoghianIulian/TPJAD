package com.finalproject.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String firstName;
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classroom_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "students", "homeroomTeacher"})
    private Classroom classroom;

    @Column(nullable = false, unique = true)
    private String registrationCode;
    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<Parent> parents;


}
