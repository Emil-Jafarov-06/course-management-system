package com.example.finalprojectcoursemanagementsystem.controller;
import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/id/{id}")
    public ResponseEntity<CourseDTO> getCourseInfo(@PathVariable @Positive Long id){
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CourseDTO> getCourseInfo(@PathVariable @NotBlank String name){
        return ResponseEntity.ok(courseService.getCourseByName(name));
    }

    @GetMapping("/search/{nameLike}")
    public ResponseEntity<List<CourseDTO>> getCoursesByNameLike(@PathVariable(name = "nameLike") @NotBlank String name){
        return ResponseEntity.ok(courseService.searchForCourses(name));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<List<CourseDTO>> getCoursesByTeacherId(@PathVariable @Positive Long id){
        return ResponseEntity.ok(courseService.getCoursesFromTeacher(id));
    }

    @GetMapping("/{courseId}/continue")
    public ResponseEntity<CourseDTO> continueCourse(@PathVariable @Positive Long courseId){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/course")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody @Valid CourseCreateRequest courseCreateRequest){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(courseService.createCourse(securityUser, courseCreateRequest));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/course/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable @Positive Long id,
                                                  @RequestBody @Valid CourseUpdateRequest courseUpdateRequest){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(courseService.updateCourse(securityUser.getCourseUser().getId(), id, courseUpdateRequest));
    }

}
