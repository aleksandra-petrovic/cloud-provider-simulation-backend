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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        //todo izbaciti bacanje greske, (exception handler)
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        User user = userService.findByEmail(loginRequest.getUsername());
        //todo umesto liste stringova napraviti mapu string boolean, da bi se prosledila kao claims
        List<Permission> permissionsObjects = user.getPermissions();
        List<String> permissions = new ArrayList<>();
        for(Permission p : permissionsObjects) { permissions.add(p.getPermission()); }

        String token = jwtUtil.generateToken(loginRequest.getUsername(), permissions);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
