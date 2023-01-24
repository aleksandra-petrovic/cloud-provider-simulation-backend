package com.example.demo.controllers;

import com.example.demo.model.User;
import com.example.demo.requests.AddUserRequest;
import com.example.demo.requests.EditUserRequest;
import com.example.demo.services.PermissionService;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/home")
public class UserController {
    private final UserService userService;
    private final PermissionService permissionService;

    private JwtUtil jwtUtil;

    public UserController(UserService userService, PermissionService permissionService, JwtUtil jwtUtil){
        this.userService = userService;
        this.permissionService = permissionService;
        this.jwtUtil = jwtUtil;
    }

    public String takeJwt(String jwt){
        if(jwt != null && jwt.startsWith("Bearer ")) return jwt.substring(7);
        else return "";
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        jwt = takeJwt(jwt);

        if(!jwtUtil.isTokenExpired(jwt) &&
                jwtUtil.extractPermission(jwt,"can_read_users").equals(true)){
            List<User> users = userService.findAll();
            return ResponseEntity.ok(users);
        }else{
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/users/add-user")
    public ResponseEntity<?> addUser(@Valid @RequestBody AddUserRequest addUserRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        jwt = takeJwt(jwt);

        if(!jwtUtil.isTokenExpired(jwt) &&
                jwtUtil.extractPermission(jwt,"can_create_users").equals(true)) {
            User user = userService.addNewUser(addUserRequest.getName(),
                                                addUserRequest.getSurname(),
                                                addUserRequest.getEmail(),
                                                addUserRequest.getPassword(),
                                                permissionService.findPermissions(addUserRequest.getPermissions()));
            return ResponseEntity.ok(user);
        }else{
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/users/edit-user/{id}")
    public ResponseEntity<?> editUser(@Valid @RequestBody EditUserRequest editUserRequest, @PathVariable("id") Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        jwt = takeJwt(jwt);

        if(!jwtUtil.isTokenExpired(jwt) &&
                jwtUtil.extractPermission(jwt,"can_update_users").equals(true)) {
            return ResponseEntity.ok(userService.editUser(userService.findById(id),
                    editUserRequest.getName(),
                    editUserRequest.getSurname(),
                    editUserRequest.getEmail(),
                    permissionService.findPermissions(editUserRequest.getPermissions())));
        }else{
            return ResponseEntity.status(401).build();
        }
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt){
        jwt = takeJwt(jwt);

        if(!jwtUtil.isTokenExpired(jwt) &&
                jwtUtil.extractPermission(jwt,"can_delete_users").equals(true)) {
            userService.deleteById(id);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping(value = "/users/edit-user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable("id") Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        jwt = takeJwt(jwt);

        if(!jwtUtil.isTokenExpired(jwt) &&
                jwtUtil.extractPermission(jwt,"can_read_users").equals(true)) {
            return ResponseEntity.ok(userService.findById(id));
        }else{
            return ResponseEntity.status(401).build();
        }
    }
}
