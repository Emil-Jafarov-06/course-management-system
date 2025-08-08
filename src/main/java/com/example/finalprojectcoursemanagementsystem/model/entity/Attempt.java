package com.example.finalprojectcoursemanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Attempt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn
    private LessonProgress lessonProgress;

}
