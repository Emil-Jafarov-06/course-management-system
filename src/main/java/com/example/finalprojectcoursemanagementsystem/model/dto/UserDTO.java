package com.example.finalprojectcoursemanagementsystem.model.dto;

import com.example.finalprojectcoursemanagementsystem.model.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Positive
    private Long id;
    @NotBlank
    private String userName;
    @Email
    private String email;
    @JsonIgnore
    private String password;

    @Min(0)
    private Double balance;
    private RoleEnum role;

}
