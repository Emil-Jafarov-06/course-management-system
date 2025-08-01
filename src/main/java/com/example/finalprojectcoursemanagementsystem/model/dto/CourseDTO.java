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
    private String name;
    private String description;
    private Double price;
    private Boolean available;

}
