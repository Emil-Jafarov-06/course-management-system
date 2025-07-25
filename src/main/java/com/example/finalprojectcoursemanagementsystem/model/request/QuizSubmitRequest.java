package com.example.finalprojectcoursemanagementsystem.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitRequest {

    private List<String> answers;

}
