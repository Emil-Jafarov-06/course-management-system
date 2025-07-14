package com.example.finalprojectcoursemanagementsystem.model.dto;

import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    @NotBlank
    private String userName;
    @Email
    private String email;
    @JsonIgnore
    private String password;

    @Min(0)
    private Double balance;
    private String role;
    @JsonIgnore
    private UserProfile userProfile;
    @JsonIgnore
    private List<Course> coursesCreated = new ArrayList<>();
    @JsonIgnore
    private List<Course> paidCourses = new ArrayList<>();

}
