package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.InformationResponse;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.LessonService;
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
@RequestMapping("/lessons")
@Tag(name = "Lesson Controller", description = "Lesson related operations")
public class LessonController {

    private final LessonService lessonService;
    private final MessageSource messageSource;

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Add lesson to course", description = "Adds a new lesson to the specified course. Only accessible by the course creator.")
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
    @Operation(summary = "Delete lesson", description = "Deletes a lesson by its ID. Only accessible by the course creator.")
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
    @Operation(summary = "Update lesson", description = "Updates an existing lesson by its ID. Only accessible by the course creator.")
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

    @Operation(summary = "Get lesson by ID", description = "Retrieves the details of a specific lesson by its ID. Accessible to authorized users.")
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
