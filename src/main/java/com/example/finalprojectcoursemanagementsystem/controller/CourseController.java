package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    @GetMapping("/id/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id){

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CourseDTO> getCourseByName(@PathVariable String name){

    }

    @GetMapping("/search/{nameLike}")
    public ResponseEntity<CourseDTO> getCourseByNameLike(@PathVariable(name = "nameLike") String name){

    }

    @PostMapping("/createCourse")
    public ResponseEntity<CourseDTO> createCourse(@PathVariable(name = "nameLike") String name){

    }

}
