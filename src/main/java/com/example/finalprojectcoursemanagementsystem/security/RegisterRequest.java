package com.example.finalprojectcoursemanagementsystem.security;

import com.example.finalprojectcoursemanagementsystem.model.enums.RoleEnum;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String password;
    private RoleEnum role;
    private String email;

}
