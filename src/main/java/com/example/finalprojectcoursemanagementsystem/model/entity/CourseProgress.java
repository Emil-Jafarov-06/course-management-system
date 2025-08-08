package com.example.finalprojectcoursemanagementsystem.model.entity;

import com.example.finalprojectcoursemanagementsystem.model.enums.ProgressEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseProgress {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private CourseUser courseUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Course course;
    private ProgressEnum progress = ProgressEnum.NOT_STARTED;

    private Integer totalUnits;
    private Integer completedUnits;

}
