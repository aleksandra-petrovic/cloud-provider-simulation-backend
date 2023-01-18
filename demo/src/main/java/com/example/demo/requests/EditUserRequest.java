package com.example.demo.requests;

import com.example.demo.model.Permission;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class EditUserRequest {
    @NotBlank(message = "Name is mandatory.")
    private String name;
    @NotBlank(message = "Surname is mandatory.")
    private String surname;
    @NotBlank(message = "Email is mandatory.")
    private String email;
    private List<String> permissions;

}
