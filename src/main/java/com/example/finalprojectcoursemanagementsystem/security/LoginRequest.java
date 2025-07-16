package com.example.finalprojectcoursemanagementsystem.security;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String username;
    private String password;

}
