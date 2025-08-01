package com.example.finalprojectcoursemanagementsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateRequest {

    @NotBlank
    private String questionText;
    @Pattern(regexp = "^[A-D]$")
    private String correctVariant;
    @NotBlank
    private String variantA;
    @NotBlank
    private String variantB;
    @NotBlank
    private String variantC;
    @NotBlank
    private String variantD;

}