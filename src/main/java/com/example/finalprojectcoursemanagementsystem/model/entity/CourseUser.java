package com.example.finalprojectcoursemanagementsystem.model.entity;
import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @Column(unique = true)
    @NotBlank
    private String userName;

    @Column(unique = true)
    @Email
    private String email;

    private String encryptedPassword;

    @Min(0)
    private Double balance;

    private RoleEnum role;

    // Only available for teachers
    @OneToMany(mappedBy = "courseOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Course> coursesCreated = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_course_enrollment",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonManagedReference
    private List<Course> paidCourses = new ArrayList<>();

    @OneToMany(mappedBy = "courseUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonProgress> lessonProgressList = new ArrayList<>();

    @OneToMany(mappedBy = "courseUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseProgress> courseProgressList = new ArrayList<>();

    public static UserDTO mapIntoDTO(CourseUser courseUser) {
        return UserDTO.builder()
                .id(courseUser.getId())
                .email(courseUser.getEmail())
                .password(courseUser.getEncryptedPassword())
                .balance(courseUser.getBalance())
                .userName(courseUser.getUserName())
                .role(courseUser.getRole()).build();
    }

    public void enrollCourse(Course course) {
        paidCourses.add(course);
    }

    public void createCourse(Course course) {
        coursesCreated.add(course);
        course.setCourseOwner(this);
    }

}
