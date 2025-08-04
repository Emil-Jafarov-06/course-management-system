package com.example.finalprojectcoursemanagementsystem.controller;

import com.example.finalprojectcoursemanagementsystem.model.PageImplementation;
import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.request.AccountDeleteRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.UsernamePasswordUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.InformationResponse;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import com.example.finalprojectcoursemanagementsystem.service.CourseService;
import com.example.finalprojectcoursemanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
@RequestMapping("")
@Tag(name = "User Controller", description = "User related operations")
public class UserController {

    private final UserService userService;
    private final CourseService courseService;
    private final MessageSource messageSource;

    @PreAuthorize("hasRole('ADMIN') || hasRole('HEAD_ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieves the list of all users. Only accessible by admins and head admins.")
    @GetMapping("users/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('HEAD_ADMIN')")
    @Operation(summary = "Add new admin", description = "Changes the role of a user to ADMIN. Only accessible by admins and head admins.")
    @PutMapping("set/admin/{userId}")
    public ResponseEntity<InformationResponse<UserDTO>> addNewAdmin(@PathVariable Long userId,
                                                                    @RequestHeader(required = false) Locale locale){
        UserDTO user = userService.addNewAdmin(userId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.set.admin.add", null, locale),
                user));
    }

    @PreAuthorize("hasRole('HEAD_ADMIN')")
    @Operation(summary = "Remove admin", description = "Changes the role of an admin to LEARNER. Only accessible by head admins.")
    @DeleteMapping("set/admin/{userId}")
    public ResponseEntity<InformationResponse<UserDTO>> deleteAdmin(@PathVariable Long userId,
                                                                   @RequestHeader(required = false) Locale locale){
        UserDTO user = userService.takeAdminRoleFromUser(userId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.set.admin.remove", null, locale),
                user));
    }

    @Operation(summary = "Get user by name", description = "Retrieves a user by name.")
    @GetMapping("users/name/{name}")
    public ResponseEntity<InformationResponse<UserDTO>> getUserByName(@PathVariable @NotBlank String name,
                                                                      @RequestHeader(required = false) Locale locale) {
        UserDTO user = userService.getUserDTOByUserName(name);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.get.byName", null, locale),
                user));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Get courses user created", description = "Retrieves the list of courses that a user has created. Only accessible by teachers.")
    @GetMapping("/created_courses")
    public ResponseEntity<InformationResponse<PageImplementation<CourseDTO>>> getCreatedCourses(@RequestParam(defaultValue = "0") int page,
                                                                                                @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageImplementation<CourseDTO> courses = userService.getCreatedCourses(securityUser.getCourseUser().getId(), page);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.created.get", null, locale),
                courses));
    }

    @Operation(summary = "Get user by id", description = "Retrieves a user by id.")
    @GetMapping("users/{id}")
    public ResponseEntity<InformationResponse<UserDTO>> getUserInfo(@PathVariable @Positive Long id,
                                                                    @RequestHeader(required = false) Locale locale) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.get.byId", null, locale),
                user));
    }

    @Operation(summary = "Search for user", description = "Retrieves all the users having username alike the inserted name.")
    @GetMapping("/users/search/{nameLike}")
    public ResponseEntity<InformationResponse<PageImplementation<UserDTO>>> getUsersByNameLike(@PathVariable(name = "nameLike") @NotBlank String name,
                                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                                 @RequestHeader(required = false) Locale locale) {
        PageImplementation<UserDTO> users = userService.getUsersByNameLike(name, page);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.search.byName", null, locale),
                users));
    }

    @Operation(summary = "Get user by email", description = "Retrieves a user by email.")
    @GetMapping("users/email/{email}")
    public ResponseEntity<InformationResponse<UserDTO>> getUserByEmail(@PathVariable @Email String email,
                                                                       @RequestHeader(required = false) Locale locale) {
        UserDTO user = userService.getUserByUserEmail(email);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.get.byEmail", null, locale),
                user));
    }

    @Operation(summary = "Get my account info", description = "Retrieves the account info of the logged in user.")
    @GetMapping("/my_account")
    public ResponseEntity<InformationResponse<UserDTO>> getAccountInfo(@RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userService.getUserById(securityUser.getCourseUser().getId());
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.account.info", null, locale),
                user));
    }

    @Operation(summary = "Get purchased courses", description = "Retrieves the list of courses that the logged-in user has purchased.")
    @GetMapping("/purchased_courses")
    public ResponseEntity<InformationResponse<PageImplementation<CourseDTO>>> getPurchasedCourses(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                                    @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageImplementation<CourseDTO> courses = userService.getPurchasedCourses(securityUser.getCourseUser().getId(), page);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.purchased.get", null, locale),
                courses));
    }

    @Operation(summary = "Register for a course", description = "Registers the logged-in user for a course by course ID.")
    @PostMapping("register/{courseId}")
    public ResponseEntity<InformationResponse<String>> registerForCourse(@PathVariable @Positive Long courseId,
                                                                         @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        courseService.enrollForCourse(securityUser, courseId);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("course.register.success", null, locale),
                "Course ID: " + courseId));
    }

    @Operation(summary = "Increase balance", description = "Increases the account balance of the logged-in user by the specified amount.")
    @PutMapping("/balance/increase/{amount}")
    public ResponseEntity<InformationResponse<String>> increaseBalance(@PathVariable @Positive Double amount,
                                                                       @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Double newBalance = userService.increaseBalance(securityUser, amount);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.balance.increase", new Object[]{newBalance}, locale),
                "New balance: " + newBalance));
    }

    @Operation(summary = "Decrease balance", description = "Decreases the account balance of the logged-in user by the specified amount.")
    @PutMapping("/balance/decrease/{amount}")
    public ResponseEntity<InformationResponse<String>> decreaseBalance(@PathVariable @Positive Double amount,
                                                                       @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Double newBalance = userService.decreaseBalance(securityUser, amount);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.balance.decrease", new Object[]{newBalance}, locale),
                "New balance: " + newBalance));
    }

    @Operation(summary = "Update email", description = "Updates the email of the logged-in user.")
    @PutMapping("/my_account/email")
    public ResponseEntity<InformationResponse<UserDTO>> updateEmail(@RequestBody @Email String email,
                                                                    @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userService.changeEmail(securityUser, email);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.email.update", null, locale),
                user));
    }

    @Operation(summary = "Update username and password", description = "Updates the username and password of the logged-in user.")
    @PutMapping("/my_account/unp")
    public ResponseEntity<InformationResponse<UserDTO>> updateUsernamePassword(@RequestBody @Valid UsernamePasswordUpdateRequest request,
                                                                               @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userService.updateUsernamePassword(securityUser, request);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.unp.update", null, locale),
                user));
    }

    @Operation(summary = "Delete my account", description = "Deletes the account of the currently logged-in user. Requires confirmation via password.")
    @DeleteMapping("/my_account")
    public ResponseEntity<InformationResponse<String>> deleteMyAccount(@RequestBody @Valid AccountDeleteRequest request,
                                                                       @RequestHeader(required = false) Locale locale) {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteAccount(securityUser, request);
        return ResponseEntity.ok(new InformationResponse<>(true,
                messageSource.getMessage("user.account.delete", null, locale),
                null));
    }


}
