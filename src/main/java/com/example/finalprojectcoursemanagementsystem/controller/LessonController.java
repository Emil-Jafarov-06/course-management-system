package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    @GetMapping("/course/{id}/lesson/{lessonId}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable("id") Long courseId, @PathVariable Long lessonId) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/course/{id}/addLesson")
    public ResponseEntity<LessonDTO> addLesson(@PathVariable Long courseId){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

}
