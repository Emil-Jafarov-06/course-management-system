package com.example.finalprojectcoursemanagementsystem.model.entity;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuizDTO;
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

    private Integer duration;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn
    private Lesson lesson;

    @OneToMany(mappedBy = "quiz", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    public static QuizDTO mapIntoDTO(Quiz quiz){
        return QuizDTO.builder()
                .id(quiz.getId())
                .quizDescription(quiz.getQuizDescription())
                .duration(quiz.getDuration()).build();
    }

}
