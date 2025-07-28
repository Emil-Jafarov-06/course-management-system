package com.example.finalprojectcoursemanagementsystem.model.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Long id;
    private String courseName;
    private String courseDescription;
    @Positive
    private Double coursePay;

}
