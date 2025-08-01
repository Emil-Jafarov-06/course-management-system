package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.InformationResponse;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.QuestionService;
import com.example.finalprojectcoursemanagementsystem.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Question Controller", description = "Question related operations")
public class QuestionController {

    private final QuestionService questionService;
    private final MessageSource messageSource;

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Get question by id", description = "Retrieves a question by id. Only accessible by the course creator.")
    @GetMapping("/{id}")
    public ResponseEntity<InformationResponse<QuestionDTO>> getQuestionById(@PathVariable @Positive Long id,
                                                                            @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionDTO question = questionService.getQuestionById(securityUser.getCourseUser().getId(), id);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("question.get.success", null, locale),
                question));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Update question by id", description = "Updates a question retrieved by id. Only accessible by the course creator.")
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
