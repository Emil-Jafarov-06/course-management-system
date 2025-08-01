package com.example.finalprojectcoursemanagementsystem.model.dto;

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
    private Double coursePay;
    private Boolean available;

}
