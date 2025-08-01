package com.example.finalprojectcoursemanagementsystem.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizUpdateRequest {

    private String description;
    private Integer duration;

}
