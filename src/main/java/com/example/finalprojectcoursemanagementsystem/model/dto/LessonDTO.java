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
    private String lessonName;
    private String lessonDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String videoURL;
    private String lessonText;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long quizId;

}
