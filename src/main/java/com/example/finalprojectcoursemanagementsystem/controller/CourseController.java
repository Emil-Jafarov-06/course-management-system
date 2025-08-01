package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.PageImplementation;
import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.InformationResponse;
import com.example.finalprojectcoursemanagementsystem.model.response.LessonResponseForInfo;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
public class CourseController {

    private final CourseService courseService;
    private final MessageSource messageSource;

    @PreAuthorize("hasRole('TEACHER')")
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

    @GetMapping("{courseId}/lessons")
    public ResponseEntity<InformationResponse<List<LessonResponseForInfo>>> getLessons(@PathVariable @Positive Long courseId,
                                                                  @RequestHeader(required = false) Locale locale){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<LessonResponseForInfo> lessons = courseService.getLessonsInfoForCourse(securityUser.getCourseUser().getId(), courseId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.lessons", null, locale),
                lessons));
    }

    @GetMapping("/{courseId}/continue")
    public ResponseEntity<InformationResponse<LessonDTO>> continueCourse(@PathVariable @Positive Long courseId,
                                                                         @RequestHeader(required = false) Locale locale){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LessonDTO lessonDTO = courseService.continueCourse(securityUser.getCourseUser().getId(), courseId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.continue", null, locale),
                lessonDTO));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<InformationResponse<CourseDTO>> getCourseInfo(@PathVariable @Positive Long id,
                                                                        @RequestHeader(required = false) Locale locale){
        CourseDTO courseDTO = courseService.getCourseById(id);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.info", null, locale),
                courseDTO));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<InformationResponse<CourseDTO>> getCourseInfo(@PathVariable @NotBlank String name,
                                                                        @RequestHeader(required = false) Locale locale){
        CourseDTO courseDTO = courseService.getCourseByName(name);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.info", null, locale),
                courseDTO));
    }

    @GetMapping("/search/{nameLike}")
    public ResponseEntity<InformationResponse<PageImplementation<CourseDTO>>> getCoursesByNameLike(@PathVariable(name = "nameLike") @NotBlank String name,
                                                                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                                                   @RequestHeader(required = false) Locale locale){
        PageImplementation<CourseDTO> courses = courseService.searchForCourses(name, page);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.search",  null, locale),
                courses));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<InformationResponse<PageImplementation<CourseDTO>>> getCoursesByTeacherId(@PathVariable @Positive Long id,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                 @RequestHeader(required = false) Locale locale){
        PageImplementation<CourseDTO> courses = courseService.getCoursesFromTeacher(id, page);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("courses.byTeacher",  null, locale),
                courses));
    }

}
