package com.example.finalprojectcoursemanagementsystem.model.entity;

import com.example.finalprojectcoursemanagementsystem.model.enums.ProgressEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LessonProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "course_user_id")
    private CourseUser courseUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private ProgressEnum progress = ProgressEnum.NOT_STARTED;

    private LocalDateTime quizStarted;

}
