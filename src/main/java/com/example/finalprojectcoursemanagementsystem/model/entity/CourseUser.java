package com.example.finalprojectcoursemanagementsystem.model.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
public class CourseUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String userName;
    @Email
    private String email;

    private String encryptedPassword;

    @Min(0)
    private Double balance;

    private String role;

    @OneToOne(cascade = CascadeType.ALL, fetch =  FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private UserProfile userProfile;

    // Only available for teachers
    @OneToMany(mappedBy = "courseOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> coursesCreated = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_course_enrollment",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> paidCourses = new ArrayList<>();

}
