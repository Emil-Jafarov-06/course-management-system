package com.example.finalprojectcoursemanagementsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountDeleteRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
