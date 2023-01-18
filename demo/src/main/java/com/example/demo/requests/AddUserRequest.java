package com.example.demo.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class AddUserRequest {
    @NotBlank(message = "Name is mandatory.")
    private String name;
    @NotBlank(message = "Surname is mandatory.")
    private String surname;
    @NotBlank(message = "Email is mandatory.")
    private String email;
    @NotBlank(message = "Password is mandatory.")
    private String password;
    private List<String> permissions;
}
