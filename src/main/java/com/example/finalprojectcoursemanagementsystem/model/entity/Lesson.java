package com.example.finalprojectcoursemanagementsystem.model.entity;

import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lessonName;
    private String lessonDescription;
    private String videoURL;
    private String lessonText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonManagedReference
    private Course course;

    @OneToOne(mappedBy = "lesson")
    private Quiz quiz;

    public static LessonDTO mapIntoDTO(Lesson lesson){
        return LessonDTO.builder()
                .id(lesson.getId())
                .lessonName(lesson.getLessonName())
                .lessonDescription(lesson.getLessonDescription())
                .lessonText(lesson.getLessonText())
                .videoURL(lesson.getVideoURL()).build();
    }

}
