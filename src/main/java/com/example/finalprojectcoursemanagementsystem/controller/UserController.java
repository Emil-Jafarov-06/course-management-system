package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.request.UserCreateRequest;
import com.example.finalprojectcoursemanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserDTO> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getUserByUserName(name));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByUserEmail(email));
    }

    @GetMapping("/myAccount")
    public ResponseEntity<UserDTO> getMyAccount() {

    }

    @GetMapping("/getPurchasedCourses")
    public ResponseEntity<List<Course>> getPurchasedCourses() {

    }

    @PostMapping("/myAccount/increaseBalance/{amount}")
    public ResponseEntity<UserDTO> increaseBalance(@PathVariable Double amount) {

    }

    @PostMapping("/myAccount/decreaseBalance/{amount}")
    public ResponseEntity<UserDTO> decreaseBalance(@PathVariable Double amount) {

    }
    @PostMapping("/myAccount/registerCourse/{courseId}")
    public ResponseEntity<String> registerForCourse(@PathVariable Long courseId) {

    }

    @PutMapping("/myAccount")
    public ResponseEntity<UserDTO> updateMyAccount(@RequestBody UserDTO userDTO) {

    }

    @DeleteMapping("/myAccount")
    public ResponseEntity<UserDTO> deleteMyAccount(@RequestBody UserDTO userDTO) {

    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserCreateRequest user) {
        return  ResponseEntity.ok(userService.registerUser(user));
    }



}
