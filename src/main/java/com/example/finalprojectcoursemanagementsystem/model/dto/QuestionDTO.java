package com.example.finalprojectcoursemanagementsystem.model.dto;

import com.example.finalprojectcoursemanagementsystem.model.entity.Quiz;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    Long id;
    String questionText;
    private Quiz quiz;
    @JsonIgnore
    private String correctVariant;
    private String variantA;
    private String variantB;
    private String variantC;
    private String variantD;

}
