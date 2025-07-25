package com.example.finalprojectcoursemanagementsystem.model.entity;

import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
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

    @Column(unique = true)
    private String courseName;
    private String courseDescription;
    private Double coursePay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_owner_id")
    @JsonManagedReference
    private CourseUser courseOwner;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToMany(mappedBy = "paidCourses", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<CourseUser> enrolledUsers = new ArrayList<>();

    public static CourseDTO mapIntoDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .courseDescription(course.getCourseDescription())
                .coursePay(course.getCoursePay())
                .build();
    }

    public void addLearner(CourseUser courseUser) {
        this.enrolledUsers.add(courseUser);
        courseUser.enrollCourse(this);
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setCourse(this);
    }

}
