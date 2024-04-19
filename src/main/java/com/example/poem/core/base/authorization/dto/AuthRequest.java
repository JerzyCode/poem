package com.example.poem.core.base.authorization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
    @NotBlank(message = "Username cannot be blank")
    @Email(message = "Invalid email format")
    String username,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    String password) {

}