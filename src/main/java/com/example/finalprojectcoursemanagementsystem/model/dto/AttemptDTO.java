package com.example.finalprojectcoursemanagementsystem.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttemptDTO {

    private Long id;
    private Double score;
    private LocalDateTime date;

}
