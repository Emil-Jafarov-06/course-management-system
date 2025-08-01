package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.InformationResponse;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.LessonService;
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
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final MessageSource messageSource;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/course/{id}/addLesson")
    public ResponseEntity<InformationResponse<LessonDTO>> addLesson(@PathVariable("id") @Positive Long courseId,
                                                                    @RequestBody @Valid LessonCreateRequest request,
                                                                    @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LessonDTO lesson = lessonService.addLesson(securityUser.getCourseUser().getId(), courseId, request);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("lesson.add.success", null, locale),
                lesson));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<InformationResponse<String>> deleteLesson(@PathVariable @Positive Long lessonId,
                                                                    @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String response = lessonService.deleteLesson(securityUser.getCourseUser().getId(), lessonId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("lesson.delete.success", null, locale),
                response));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{lessonId}")
    public ResponseEntity<InformationResponse<LessonDTO>> updateLesson(@PathVariable @Positive Long lessonId,
                                                                       @RequestBody @Valid LessonUpdateRequest request,
                                                                       @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LessonDTO lesson = lessonService.updateLesson(securityUser.getCourseUser().getId(), lessonId, request);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("lesson.update.success", null, locale),
                lesson));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<InformationResponse<LessonDTO>> getLesson(@PathVariable @Positive Long lessonId,
                                                                    @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LessonDTO lesson = lessonService.getLesson(securityUser.getCourseUser().getId(), lessonId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("lesson.get.success", null, locale),
                lesson));
    }

}
