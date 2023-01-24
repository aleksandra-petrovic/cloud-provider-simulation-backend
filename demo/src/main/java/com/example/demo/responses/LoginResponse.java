package com.example.demo.responses;

import com.example.demo.model.Permission;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String jwt;
    public LoginResponse(String jwt) {
     this.jwt = jwt;
    }
}
