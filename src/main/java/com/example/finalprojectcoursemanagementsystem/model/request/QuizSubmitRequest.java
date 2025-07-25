package com.example.finalprojectcoursemanagementsystem.model.request;

import lombok.*;

import javax.swing.plaf.synth.SynthTabbedPaneUI;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitRequest {

    private Map<Long, String> answers;

}
