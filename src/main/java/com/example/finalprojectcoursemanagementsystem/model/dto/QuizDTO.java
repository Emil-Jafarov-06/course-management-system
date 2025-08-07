package com.example.finalprojectcoursemanagementsystem.model.dto;

import com.example.finalprojectcoursemanagementsystem.model.entity.Question;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String description;
    private Integer duration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<QuestionDTO> questions = new ArrayList<>();

}
