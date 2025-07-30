package com.example.finalprojectcoursemanagementsystem.controller;
import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.LessonResponseForInfo;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/course")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody @Valid CourseCreateRequest courseCreateRequest){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(courseService.createCourse(securityUser, courseCreateRequest));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable @Positive Long id,
                                                  @RequestBody @Valid CourseUpdateRequest courseUpdateRequest){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(courseService.updateCourse(securityUser.getCourseUser().getId(), id, courseUpdateRequest));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{courseId}/enrolledUsers")
    public ResponseEntity<Page<UserDTO>> getEnrolledUsers(@PathVariable @Positive Long courseId, @RequestParam(defaultValue = "0") @PositiveOrZero int page){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  ResponseEntity.ok(courseService.getEnrolledUsers(securityUser.getCourseUser().getId(), courseId, page));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PatchMapping("/{courseId}/setAvailable")
    public ResponseEntity<CourseDTO> setCourseAvailable(@PathVariable @Positive Long courseId){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(courseService.setCourseAvailable(securityUser.getCourseUser().getId(), courseId));
    }

    @GetMapping("{courseId}/lessons")
    public ResponseEntity<List<LessonResponseForInfo>> getLessons(@PathVariable @Positive Long courseId){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(courseService.getLessonsInfoForCourse(securityUser.getCourseUser().getId(), courseId));
    }

    @GetMapping("/{courseId}/continue")
    public ResponseEntity<LessonDTO> continueCourse(@PathVariable @Positive Long courseId){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(courseService.continueCourse(securityUser.getCourseUser().getId(), courseId));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CourseDTO> getCourseInfo(@PathVariable @Positive Long id){
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CourseDTO> getCourseInfo(@PathVariable @NotBlank String name){
        return ResponseEntity.ok(courseService.getCourseByName(name));
    }

    @GetMapping("/search/{nameLike}")
    public ResponseEntity<Page<CourseDTO>> getCoursesByNameLike(@PathVariable(name = "nameLike") @NotBlank String name, @RequestParam(defaultValue = "0") @PositiveOrZero int page){
        return ResponseEntity.ok(courseService.searchForCourses(name, page));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<Page<CourseDTO>> getCoursesByTeacherId(@PathVariable @Positive Long id, @RequestParam(defaultValue = "0") @PositiveOrZero int page){
        return ResponseEntity.ok(courseService.getCoursesFromTeacher(id, page));
    }

}
