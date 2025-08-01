package com.example.finalprojectcoursemanagementsystem.model.entity;

import com.example.finalprojectcoursemanagementsystem.model.enums.ProgressEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private Integer attemptCount;
    private Double bestScore;

    @OneToMany(mappedBy = "lessonProgress", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attempt> attempts = new ArrayList<>();

    public void addAttempt(Attempt attempt) {
        attempts.add(attempt);
        attempt.setLessonProgress(this);
    }

}
