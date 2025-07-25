package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.AccountDeleteRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.UsernamePasswordUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.CourseService;
import com.example.finalprojectcoursemanagementsystem.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CourseService courseService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserDTO> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getUserDTOByUserName(name));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByUserEmail(email));
    }

    @GetMapping("/myAccount")
    public ResponseEntity<UserDTO> getAccountInfo() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getUserById(securityUser.getCourseUser().getId()));
    }

    @GetMapping("/purchasedCourses")
    public ResponseEntity<List<CourseDTO>> getPurchasedCourses() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getPurchasedCourses(securityUser.getCourseUser().getId()));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/createdCourses")
    public ResponseEntity<List<CourseDTO>> getCreatedCourses() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getCreatedCourses(securityUser.getCourseUser().getId()));
    }

    @PutMapping("/balance/increase/{amount}")
    public ResponseEntity<String> increaseBalance(@PathVariable @Positive Double amount) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("Current balance: " + userService.increaseBalance(securityUser, amount));
    }

    @PutMapping("/balance/decrease/{amount}")
    public ResponseEntity<String> decreaseBalance(@PathVariable @Positive Double amount) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("Current balance: " + userService.decreaseBalance(securityUser, amount));
    }

    @PostMapping("register/{courseId}")
    public ResponseEntity<String> registerForCourse(@PathVariable Long courseId) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        courseService.enrollForCourse(securityUser, courseId);
        return ResponseEntity.ok("Enrolled for course " + courseId);
    }

    @PutMapping("/myAccount/email")
    public ResponseEntity<UserDTO> updateEmail(@RequestBody @Email String email) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.changeEmail(securityUser, email));
    }

    @PutMapping("/myAccount/unp")
    public ResponseEntity<UserDTO> updateUsernamePassword(@RequestBody UsernamePasswordUpdateRequest request) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.updateUsernamePassword(securityUser, request));
    }

    @DeleteMapping("/myAccount")
    public ResponseEntity<String> deleteMyAccount(@RequestBody AccountDeleteRequest request) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteAccount(securityUser, request);
        return ResponseEntity.ok("Account deleted!");
    }

}
