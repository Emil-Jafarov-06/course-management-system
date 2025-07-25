package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.QuizDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizSubmitRequest;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> takeQuiz(@PathVariable("id") Long id){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(quizService.getQuiz(securityUser.getCourseUser().getId() ,id));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<QuizDTO> addQuiz(@RequestBody QuizCreateRequest request){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(quizService.createQuiz(securityUser.getCourseUser().getId(), request));
    }
//
//    @PostMapping("/{id}")
//    public ResponseEntity<String> submitQuiz(@PathVariable("id") Long quizId, @RequestBody QuizSubmitRequest request){
//        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        return ResponseEntity.ok(quizService.checkQuiz(securityUser.getCourseUser().getId(), quizId, request));
//    }

    @PostMapping("/{id}/question")
    public ResponseEntity<QuestionDTO> addQuestionToQuiz(@PathVariable("id") Long quizId, @RequestBody QuestionCreateRequest request){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(quizService.addQuestionToQuiz(securityUser.getCourseUser().getId(), quizId, request));

    }


}
