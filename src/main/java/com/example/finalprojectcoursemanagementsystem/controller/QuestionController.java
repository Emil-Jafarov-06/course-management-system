package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.InformationResponse;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.QuestionService;
import com.example.finalprojectcoursemanagementsystem.service.QuizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final MessageSource messageSource;

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{id}")
    public ResponseEntity<InformationResponse<QuestionDTO>> getQuestionById(@PathVariable @Positive Long id,
                                                                            @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionDTO question = questionService.getQuestionById(securityUser.getCourseUser().getId(), id);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("question.get.success", null, locale),
                question));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InformationResponse<QuestionDTO>> updateQuestionById(@PathVariable("id") @Positive Long questionId,
                                                                               @Valid @RequestBody QuestionUpdateRequest request,
                                                                               @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionDTO updated = questionService.updateQuestionById(securityUser.getCourseUser().getId(), questionId, request);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("question.update.success", null, locale),
                updated));
    }

}
