package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.LessonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/course/{id}/addLesson")
    public ResponseEntity<LessonDTO> addLesson(@PathVariable("id") @Positive Long courseId,
                                               @RequestBody @Valid LessonCreateRequest request){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(lessonService.addLesson(securityUser.getCourseUser().getId() ,courseId, request));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable @Positive Long lessonId){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(lessonService.deleteLesson(securityUser.getCourseUser().getId(), lessonId));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{lessonId}")
    public ResponseEntity<?> updateLesson(@PathVariable @Positive Long lessonId, @RequestBody @Valid LessonUpdateRequest request){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(lessonService.updateLesson(securityUser.getCourseUser().getId(), lessonId, request));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable @Positive Long lessonId) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(lessonService.getLesson(securityUser.getCourseUser().getId(), lessonId));
    }

}
