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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String questionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Quiz quiz;

    private String correctAnswer;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.DETACH)
    List<Answer> answers = new ArrayList<>();

}
