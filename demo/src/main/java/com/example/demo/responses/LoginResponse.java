package com.example.demo.responses;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwt;

    //private List<String> permissions;

    //public LoginResponse(String jwt, List<String> permissions) {
    // this.jwt = jwt;
    // this.permissions = permissions;
    // }
    public LoginResponse(String jwt) { this.jwt = jwt; }
}
