package com.example.finalprojectcoursemanagementsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordUpdateRequest {

    @NotBlank
    String oldUsername;
    @NotBlank
    String newUsername;
    @NotBlank
    String oldPassword;
    @NotBlank
    String newPassword;

}
