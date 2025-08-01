package com.example.finalprojectcoursemanagementsystem.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateRequest {

    @Positive
    private Long lessonId;
    @NotBlank
    private String description;
    @Positive
    private Integer duration;

}
