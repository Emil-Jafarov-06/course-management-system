package com.example.finalprojectcoursemanagementsystem.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank
    private String userName;
    @Email
    private String email;
    @NotBlank
    private String password;

}
