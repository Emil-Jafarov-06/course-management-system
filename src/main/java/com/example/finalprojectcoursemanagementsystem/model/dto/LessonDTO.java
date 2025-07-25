package com.example.finalprojectcoursemanagementsystem.model.dto;

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
    private String videoURL;
    private String lessonText;

}
