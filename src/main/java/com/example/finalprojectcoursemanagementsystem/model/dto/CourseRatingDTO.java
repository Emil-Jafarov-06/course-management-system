package com.example.finalprojectcoursemanagementsystem.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseRatingDTO {

    private Long courseId;
    private Double rating;
    private Long raterCount;

}
