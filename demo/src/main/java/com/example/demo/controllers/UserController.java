package com.example.demo.controllers;

import com.example.demo.model.User;
import com.example.demo.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/home")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers() {
        List<User> users;
        try {
            users = userService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(users);
    }



}
