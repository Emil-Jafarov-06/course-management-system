package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.PageImplementation;
import com.example.finalprojectcoursemanagementsystem.model.dto.*;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseProgress;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.InformationResponse;
import com.example.finalprojectcoursemanagementsystem.model.response.LessonResponseForInfo;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
@Tag(name = "Course Controller", description = "Course related operations")
public class CourseController {

    private final CourseService courseService;
    private final MessageSource messageSource;

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Create a course", description = "Allows a teacher to create a new course.")
    @PostMapping("/course")
    public ResponseEntity<InformationResponse<CourseDTO>> createCourse(@RequestBody @Valid CourseCreateRequest courseCreateRequest,
                                                  @RequestHeader(required = false) Locale locale){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CourseDTO courseDTO = courseService.createCourse(securityUser, courseCreateRequest);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.created", null, locale),
                courseDTO));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Update course", description = "Updates an existing course by ID. Only the course creator can perform this action.")
    @PutMapping("/{id}")
    public ResponseEntity<InformationResponse<CourseDTO>> updateCourse(@PathVariable @Positive Long id,
                                                  @RequestBody @Valid CourseUpdateRequest courseUpdateRequest,
                                                                       @RequestHeader(required = false) Locale locale){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CourseDTO courseDTO = courseService.updateCourse(securityUser.getCourseUser().getId(), id, courseUpdateRequest);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.updated", null, locale),
                courseDTO));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Get enrolled users", description = "Returns a paginated list of users enrolled in a specific course. Accessible only by the course creator.")
    @GetMapping("/{courseId}/enrolled_users")
    public ResponseEntity<InformationResponse<PageImplementation<UserDTO>>> getEnrolledUsers(@PathVariable @Positive Long courseId,
                                                                        @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                        @RequestHeader(required = false) Locale locale){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageImplementation<UserDTO> users = courseService.getEnrolledUsers(securityUser.getCourseUser().getId(), courseId, page);
        return  ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.enrolled.users", null, locale),
                users));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Set course availability", description = "Sets the course's availability status to true or false. Only the course creator can modify this.")
    @PatchMapping("/{courseId}/set_available")
    public ResponseEntity<InformationResponse<CourseDTO>> setCourseAvailable(@PathVariable @Positive Long courseId,
                                                                             @RequestBody(required = false) boolean available,
                                                                             @RequestHeader(required = false) Locale locale){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CourseDTO courseDTO = courseService.setCourseAvailability(securityUser.getCourseUser().getId(), courseId, available);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.availability", null, locale),
                courseDTO));
    }

    @Operation(summary = "Get lessons of a course", description = "Retrieves a list of all lessons for the specified course. The user must be enrolled or the course must be public.")
    @GetMapping("{courseId}/lessons")
    public ResponseEntity<InformationResponse<List<LessonResponseForInfo>>> getLessons(@PathVariable @Positive Long courseId,
                                                                  @RequestHeader(required = false) Locale locale){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<LessonResponseForInfo> lessons = courseService.getLessonsInfoForCourse(securityUser.getCourseUser().getId(), courseId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.lessons", null, locale),
                lessons));
    }

    @Operation(summary = "Continue course", description = "Returns the next lesson the logged-in user should take to continue the specified course.")
    @GetMapping("/{courseId}/continue")
    public ResponseEntity<InformationResponse<LessonDTO>> continueCourse(@PathVariable @Positive Long courseId,
                                                                         @RequestHeader(required = false) Locale locale){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LessonDTO lessonDTO = courseService.continueCourse(securityUser.getCourseUser().getId(), courseId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.continue", null, locale),
                lessonDTO));
    }

    @Operation(summary = "Get course by ID", description = "Fetches detailed information of a course by its unique ID.")
    @GetMapping("/id/{id}")
    public ResponseEntity<InformationResponse<CourseDTO>> getCourseInfo(@PathVariable @Positive Long id,
                                                                        @RequestHeader(required = false) Locale locale){
        CourseDTO courseDTO = courseService.getCourseById(id);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.info", null, locale),
                courseDTO));
    }

    @Operation(summary = "Get course by name", description = "Fetches detailed information of a course by its unique name.")
    @GetMapping("/name/{name}")
    public ResponseEntity<InformationResponse<CourseDTO>> getCourseInfo(@PathVariable @NotBlank String name,
                                                                        @RequestHeader(required = false) Locale locale){
        CourseDTO courseDTO = courseService.getCourseByName(name);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.info", null, locale),
                courseDTO));
    }

    @Operation(summary = "Search courses by name", description = "Searches for courses whose names contain the given substring. Returns paginated results.")
    @GetMapping("/search/{nameLike}")
    public ResponseEntity<InformationResponse<PageImplementation<CourseDTO>>> getCoursesByNameLike(@PathVariable(name = "nameLike") @NotBlank String name,
                                                                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                                                   @RequestHeader(required = false) Locale locale){
        PageImplementation<CourseDTO> courses = courseService.searchForCourses(name, page);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.search",  null, locale),
                courses));
    }

    @Operation(summary = "Get courses by teacher ID", description = "Retrieves all available courses created by the specified teacher, paginated.")
    @GetMapping("/teacher/{id}")
    public ResponseEntity<InformationResponse<PageImplementation<CourseDTO>>> getCoursesByTeacherId(@PathVariable @Positive Long id,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                 @RequestHeader(required = false) Locale locale){
        PageImplementation<CourseDTO> courses = courseService.getCoursesFromTeacher(id, page);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("courses.byTeacher",  null, locale),
                courses));
    }

    @Operation(summary = "Rates the course", description = "Adds a new rating for a course and retrieves the average rating of the course.")
    @PostMapping("/rate/{id}")
    public ResponseEntity<InformationResponse<CourseRatingDTO>> rateCourse(@PathVariable @Positive Long id,
                                                                           @RequestHeader(required = false) Locale locale,
                                                                           @RequestBody @Min(0) @Max(5) Double rating){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CourseRatingDTO courseRatingDTO = courseService.rateCourse(securityUser, id, rating);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.rated", null, locale),
                courseRatingDTO));
    }

    @GetMapping("/{courseId}/progress")
    public ResponseEntity<InformationResponse<CourseProgressDTO>> getCourseProgress(@PathVariable @Positive Long courseId,
                                                                                    @RequestHeader(required = false) Locale locale){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CourseProgressDTO courseProgressDTO = courseService.getCourseProgress(securityUser.getCourseUser().getId(), courseId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.progress", null, locale),
                courseProgressDTO));
    }

}
