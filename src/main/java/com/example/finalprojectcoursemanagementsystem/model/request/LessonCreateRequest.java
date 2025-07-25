package com.example.finalprojectcoursemanagementsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreateRequest {

    @NotBlank
    private String lessonName;
    @NotBlank
    private String lessonDescription;
    @NotBlank
    private String videoURL;
    @NotBlank
    private String lessonText;

}
