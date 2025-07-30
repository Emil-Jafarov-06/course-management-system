package com.example.finalprojectcoursemanagementsystem.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponseForInfo {

    private Long id;
    private String lessonName;
    private String lessonDescription;

}