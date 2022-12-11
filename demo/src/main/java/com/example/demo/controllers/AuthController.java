package com.example.demo.controllers;

import com.example.demo.model.Permission;
import com.example.demo.model.User;
import com.example.demo.requests.LoginRequest;
import com.example.demo.responses.LoginResponse;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        User user = userService.findByEmail(loginRequest.getUsername());
        List<Permission> permissionsObjects = user.getPermissions();
        List<String> permissions = new ArrayList<>();
        for(Permission p : permissionsObjects){
            permissions.add(p.getPermission());
        }

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getUsername()), permissions));
    }

}
