package com.example.finalprojectcoursemanagementsystem.model.dto;

import com.example.finalprojectcoursemanagementsystem.model.entity.Question;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {

    private Long id;
    private String quizDescription;
    private Integer duration;
    private List<QuestionDTO> questions = new ArrayList<>();

}
