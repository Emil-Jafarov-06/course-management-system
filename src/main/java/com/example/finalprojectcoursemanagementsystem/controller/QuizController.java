package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.QuizDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizSubmitRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.InformationResponse;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
@Tag(name = "Quiz Controller", description = "Operations related to quizzes and questions")
public class QuizController {

    private final QuizService quizService;
    private final MessageSource messageSource;

    @Operation(summary = "Get quiz by ID", description = "Retrieves quiz details for the logged-in user.")
    @GetMapping("/{id}")
    public ResponseEntity<InformationResponse<QuizDTO>> getQuiz(@PathVariable("id") @Positive Long id,
                                                                @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuizDTO quiz = quizService.getQuiz(securityUser.getCourseUser().getId(), id);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("quiz.get.success", null, locale),
                quiz));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Create a quiz", description = "Creates a new quiz for a lesson. Only accessible by teachers.")
    @PostMapping
    public ResponseEntity<InformationResponse<QuizDTO>> addQuizToLesson(@RequestBody @Valid QuizCreateRequest request,
                                                                        @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuizDTO quiz = quizService.createQuiz(securityUser.getCourseUser().getId(), request);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("quiz.add.success", null, locale),
                quiz));
    }

    @Operation(summary = "Submit quiz", description = "Submits answers for a quiz and returns the result for the logged-in user.")
    @PostMapping("/{id}")
    public ResponseEntity<InformationResponse<String>> submitQuiz(@PathVariable("id") Long quizId,
                                                                  @RequestBody QuizSubmitRequest request,
                                                                  @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String result = quizService.checkQuiz(securityUser.getCourseUser().getId(), quizId, request);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("quiz.submit.success", null, locale),
                result));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Add question to quiz", description = "Adds a new question to the specified quiz. Only accessible by teachers.")
    @PostMapping("/{id}/question")
    public ResponseEntity<InformationResponse<QuestionDTO>> addQuestionToQuiz(@PathVariable("id") @Positive Long quizId,
                                                                              @RequestBody @Valid QuestionCreateRequest request,
                                                                              @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionDTO question = quizService.addQuestionToQuiz(securityUser.getCourseUser().getId(), quizId, request);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("quiz.question.add.success", null, locale),
                question));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Update quiz", description = "Updates quiz content. Only the quiz creator (teacher) can perform this action.")
    @PutMapping("/{quizId}")
    public ResponseEntity<InformationResponse<QuizDTO>> updateQuiz(@PathVariable Long quizId,
                                                                   @RequestBody QuizUpdateRequest request,
                                                                   @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuizDTO updated = quizService.updateQuiz(securityUser.getCourseUser().getId(), quizId, request);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("quiz.update.success", null, locale),
                updated));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Delete question from quiz", description = "Deletes a question from a quiz. Only accessible by the quiz creator.")
    @DeleteMapping("question/{questionId}")
    public ResponseEntity<InformationResponse<String>> deleteQuestionFromQuiz(@PathVariable @Positive Long questionId,
                                                                              @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String message = quizService.deleteQuestionFromQuiz(securityUser.getCourseUser().getId(), questionId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("quiz.question.delete.success", null, locale),
                message));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Delete quiz", description = "Deletes a quiz by its ID. Only the quiz creator (teacher) can perform this action.")
    @DeleteMapping("/{quizId}")
    public ResponseEntity<InformationResponse<String>> deleteQuiz(@PathVariable @Positive Long quizId,
                                                                  @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String message = quizService.deleteQuiz(securityUser.getCourseUser().getId(), quizId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("quiz.delete.success", null, locale),
                message));
    }

}
