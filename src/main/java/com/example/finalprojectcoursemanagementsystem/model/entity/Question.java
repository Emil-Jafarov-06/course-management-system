package com.example.finalprojectcoursemanagementsystem.model.entity;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import jakarta.persistence.*;
import lombok.*;

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

    private String correctVariant;

    private String variantA;
    private String variantB;
    private String variantC;
    private String variantD;

    public static QuestionDTO mapIntoDTO(Question question) {
        return QuestionDTO.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .correctVariant(question.getCorrectVariant())
                .variantA(question.getVariantA())
                .variantB(question.getVariantB())
                .variantC(question.getVariantC())
                .variantD(question.getVariantD()).build();
    }

}
