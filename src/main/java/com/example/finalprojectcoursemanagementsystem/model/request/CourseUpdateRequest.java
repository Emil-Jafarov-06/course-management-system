package com.example.finalprojectcoursemanagementsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Positive
    private Double price;

}
