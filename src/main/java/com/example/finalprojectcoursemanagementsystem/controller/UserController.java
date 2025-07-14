package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public Course getUserInfo(@PathVariable String id) {

    }

}
