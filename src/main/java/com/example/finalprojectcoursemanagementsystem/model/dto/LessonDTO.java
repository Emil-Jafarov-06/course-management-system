package com.example.finalprojectcoursemanagementsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LessonDTO {

    private Long id;
    private String name;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String videoURL;
    private String text;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long quizId;

}
