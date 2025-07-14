package com.example.finalprojectcoursemanagementsystem.model.entity;

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
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quizDescription;

    private Integer maxMinutes;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn
    private Lesson lesson;

    @OneToMany(mappedBy = "quiz", orphanRemoval = true, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

}
