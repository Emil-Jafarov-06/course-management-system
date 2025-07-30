package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.QuizDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizSubmitRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.QuizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable("id") @Positive Long id){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(quizService.getQuiz(securityUser.getCourseUser().getId() ,id));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<QuizDTO> addQuizToLesson(@RequestBody @Valid QuizCreateRequest request){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(quizService.createQuiz(securityUser.getCourseUser().getId(), request));
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> submitQuiz(@PathVariable("id") Long quizId,
                                             @RequestBody QuizSubmitRequest request){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(quizService.checkQuiz(securityUser.getCourseUser().getId(), quizId, request));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{id}/question")
    public ResponseEntity<QuestionDTO> addQuestionToQuiz(@PathVariable("id") @Positive Long quizId,
                                                         @RequestBody @Valid QuestionCreateRequest request){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(quizService.addQuestionToQuiz(securityUser.getCourseUser().getId(), quizId, request));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{quizId}")
    public ResponseEntity<QuizDTO> updateQuiz(@PathVariable Long quizId, @RequestBody QuizUpdateRequest request){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(quizService.updateQuiz(securityUser.getCourseUser().getId(), quizId, request));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("question/{questionId}")
    public ResponseEntity<String> deleteQuestionFromQuiz(@PathVariable @Positive Long questionId){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(quizService.deleteQuestionFromQuiz(securityUser.getCourseUser().getId(), questionId));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{quizId}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long quizId){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(quizService.deleteQuiz(securityUser.getCourseUser().getId(), quizId));
    }
}
