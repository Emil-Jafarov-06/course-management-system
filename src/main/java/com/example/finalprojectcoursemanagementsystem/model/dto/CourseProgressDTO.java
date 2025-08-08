package com.example.finalprojectcoursemanagementsystem.model.dto;

import com.example.finalprojectcoursemanagementsystem.model.enums.ProgressEnum;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseProgressDTO {

    private Long id;
    private Long courseId;
    private ProgressEnum progress;
    private Integer totalUnits;
    private Integer completedUnits;

}
