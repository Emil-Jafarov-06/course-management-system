package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.AccountDeleteRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.UsernamePasswordUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.CourseService;
import com.example.finalprojectcoursemanagementsystem.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
@RequestMapping("")
public class UserController {

    private final UserService userService;
    private final CourseService courseService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("users/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("users/name/{name}")
    public ResponseEntity<UserDTO> getUserByName(@PathVariable @NotBlank String name) {
        return ResponseEntity.ok(userService.getUserDTOByUserName(name));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/createdCourses")
    public ResponseEntity<Page<CourseDTO>> getCreatedCourses(@RequestParam(defaultValue = "0") int page) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getCreatedCourses(securityUser.getCourseUser().getId(), page));
    }

    @GetMapping("users/id/{id}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/users/search/{nameLike}")
    public ResponseEntity<Page<UserDTO>> getUsersByNameLike(@PathVariable(name = "nameLike") @NotBlank String name, @RequestParam(defaultValue = "0") @PositiveOrZero int page) {
        return ResponseEntity.ok(userService.getUsersByNameLike(name, page));
    }

    @GetMapping("users/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable @Email String email) {
        return ResponseEntity.ok(userService.getUserByUserEmail(email));
    }

    @GetMapping("/myAccount")
    public ResponseEntity<UserDTO> getAccountInfo() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getUserById(securityUser.getCourseUser().getId()));
    }

    @GetMapping("/purchasedCourses")
    public ResponseEntity<Page<CourseDTO>> getPurchasedCourses(@RequestParam(defaultValue = "0") @PositiveOrZero int page) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getPurchasedCourses(securityUser.getCourseUser().getId(), page));
    }

    @PostMapping("register/{courseId}")
    public ResponseEntity<String> registerForCourse(@PathVariable @Positive Long courseId) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        courseService.enrollForCourse(securityUser, courseId);
        return ResponseEntity.ok("Enrolled for course " + courseId);
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

    @PutMapping("/myAccount/email")
    public ResponseEntity<UserDTO> updateEmail(@RequestBody @Email String email) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.changeEmail(securityUser, email));
    }

    @PutMapping("/myAccount/unp")
    public ResponseEntity<UserDTO> updateUsernamePassword(@RequestBody @Valid UsernamePasswordUpdateRequest request) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.updateUsernamePassword(securityUser, request));
    }

    @DeleteMapping("/myAccount")
    public ResponseEntity<String> deleteMyAccount(@RequestBody @Valid AccountDeleteRequest request) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteAccount(securityUser, request);
        return ResponseEntity.ok("Account deleted!");
    }

}
