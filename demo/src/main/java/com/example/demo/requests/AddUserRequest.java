package com.example.demo.requests;

import lombok.Data;

import java.util.List;

@Data
public class AddUserRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private List<String> permissions;
}
