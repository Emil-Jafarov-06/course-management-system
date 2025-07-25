package com.example.finalprojectcoursemanagementsystem.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateRequest {

    String questionText;
    private String correctVariant;
    private String variantA;
    private String variantB;
    private String variantC;
    private String variantD;

}