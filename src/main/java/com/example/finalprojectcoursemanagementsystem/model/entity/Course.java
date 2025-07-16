package com.example.finalprojectcoursemanagementsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    private String courseDescription;
    private Double coursePay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_owner_id")
    private CourseUser courseOwner;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = true)
    @JsonBackReference
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToMany(mappedBy = "paidCourses", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<CourseUser> enrolledUsers = new ArrayList<>();

}
