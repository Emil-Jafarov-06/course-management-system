package com.example.finalprojectcoursemanagementsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @URL
    private String videoURL;
    @NotBlank
    private String text;

}
