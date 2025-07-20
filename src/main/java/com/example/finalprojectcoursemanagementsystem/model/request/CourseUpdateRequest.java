package com.example.finalprojectcoursemanagementsystem.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateRequest {

    private String courseName;
    private String courseDescription;
    private Double coursePay;

}
