package com.example.finalprojectcoursemanagementsystem.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateRequest {

    private Long lessonId;
    private String quizDescription;
    private Integer duration;

}
