package com.example.finalprojectcoursemanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String variant;

    private String answerText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Question question;

}
