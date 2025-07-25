package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.QuizDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Quiz;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonCreateRequest;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private  final LessonService lessonService;

    @GetMapping("lesson/{lessonId}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable Long lessonId) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(lessonService.getLesson(securityUser.getCourseUser().getId(), lessonId));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/course/{id}/addLesson")
    public ResponseEntity<LessonDTO> addLesson(@PathVariable("id") Long courseId, @RequestBody LessonCreateRequest request){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(lessonService.addLesson(securityUser.getCourseUser().getId() ,courseId, request));
    }

}
