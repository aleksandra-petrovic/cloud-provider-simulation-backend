package com.example.demo.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is mandatory.")
    private String username;
    @NotBlank(message = "Password is mandatory.")
    private String password;
}
