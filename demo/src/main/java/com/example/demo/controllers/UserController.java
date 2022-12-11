package com.example.demo.controllers;

import com.example.demo.model.Permission;
import com.example.demo.model.User;
import com.example.demo.requests.AddUserRequest;
import com.example.demo.requests.EditUserRequest;
import com.example.demo.requests.LoginRequest;
import com.example.demo.responses.LoginResponse;
import com.example.demo.services.PermissionService;
import com.example.demo.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/home")
public class UserController {

    private final UserService userService;
    private final PermissionService permissionService;

    public UserController(UserService userService, PermissionService permissionService){
        this.userService = userService;
        this.permissionService = permissionService;
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

    @PostMapping("/users/add-user")
    public ResponseEntity<?> addUser(@RequestBody AddUserRequest addUserRequest) {
        User user = null;
        try {
            //ime prezime email perm
            List<Permission> permissions = new ArrayList<>();
            for(String s : addUserRequest.getPermissions()){
                permissions.add(permissionService.findPermission(s));
            }

            user = userService.addNewUser(addUserRequest.getName(), addUserRequest.getSurname(), addUserRequest.getEmail(), addUserRequest.getPassword(), permissions);
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user);
//        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getUsername()), permissions));
    }

    @PostMapping("/users/edit-user/{id}")
    public ResponseEntity<?> editUser(@RequestBody EditUserRequest editUserRequest, @PathVariable("id") Long id) {

        List<Permission> permissions = new ArrayList<>();
        for(String s : editUserRequest.getPermissions()){
            permissions.add(permissionService.findPermission(s));
        }

        return ResponseEntity.ok(
            userService.editUser(userService.findById(id), editUserRequest.getName(), editUserRequest.getSurname(), editUserRequest.getEmail(),
                        permissions));
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }



}
