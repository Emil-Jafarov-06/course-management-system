package com.example.finalprojectcoursemanagementsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LessonUpdateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String videoURL;
    @NotBlank
    private String text;

}
