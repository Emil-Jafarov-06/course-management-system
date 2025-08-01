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
    private String oldUsername;
    @NotBlank
    private String newUsername;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

}
