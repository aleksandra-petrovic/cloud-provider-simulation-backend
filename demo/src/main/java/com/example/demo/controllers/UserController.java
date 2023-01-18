package com.example.demo.controllers;

import com.example.demo.model.Permission;
import com.example.demo.model.User;
import com.example.demo.requests.AddUserRequest;
import com.example.demo.requests.EditUserRequest;
import com.example.demo.requests.LoginRequest;
import com.example.demo.responses.LoginResponse;
import com.example.demo.services.PermissionService;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PermissionService permissionService;

    private JwtUtil jwtUtil;

    public UserController(UserService userService, PermissionService permissionService, AuthenticationManager authenticationManager,JwtUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.permissionService = permissionService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(@RequestParam String jwt) {

        String email = jwtUtil.extractUsername(jwt);
        User loggedUser = userService.findByEmail(email);
        boolean ok = false;

        for(Permission permission: loggedUser.getPermissions()) {
            if (permission.getPermission().equals("can_read_users")) {
                ok = true;
            }
        }

        if(ok) {
            List<User> users;
            users = userService.findAll();
            return ResponseEntity.ok(users);
        }else{

            return ResponseEntity.status(401).build();
        }


//        if(jwt.equals(userService.getJwt())) {
//            String username = jwtUtil.extractUsername(jwt);
//            UserDetails userDetails = this.userService.loadUserByUsername(username);
//            if(userDetails.isAccountNonExpired() || userDetails.isCredentialsNonExpired()){
//                List<User> users;
//                users = userService.findAll();
//                return ResponseEntity.ok(users);
//            }else {
//
//                return ResponseEntity.status(401).build();
//            }
//        }else {
//
//            return ResponseEntity.status(401).build();
//        }

    }

    @PostMapping("/users/add-user")
    public ResponseEntity<?> addUser(@RequestBody AddUserRequest addUserRequest, @RequestParam String jwt) {

        String email = jwtUtil.extractUsername(jwt);
        User loggedUser = userService.findByEmail(email);
        boolean ok = false;

        for(Permission permission: loggedUser.getPermissions()) {
            if (permission.getPermission().equals("can_create_users")) {
                ok = true;
            }
        }

        if(ok) {
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

        }else{
            return ResponseEntity.status(401).build();
        }

//
//        User user = null;
//        try {
//            //ime prezime email perm
//            List<Permission> permissions = new ArrayList<>();
//            for(String s : addUserRequest.getPermissions()){
//                permissions.add(permissionService.findPermission(s));
//            }
//
//            user = userService.addNewUser(addUserRequest.getName(), addUserRequest.getSurname(), addUserRequest.getEmail(), addUserRequest.getPassword(), permissions);
////            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//        } catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(401).build();
//        }
//
//        return ResponseEntity.ok(user);
////        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getUsername()), permissions));
    }

    @PostMapping("/users/edit-user/{id}")
    public ResponseEntity<?> editUser(@RequestBody EditUserRequest editUserRequest, @PathVariable("id") Long id, @RequestParam String jwt) {

        String email = jwtUtil.extractUsername(jwt);
        User loggedUser = userService.findByEmail(email);
        boolean ok = false;

        for(Permission permission: loggedUser.getPermissions()) {
            if (permission.getPermission().equals("can_update_users")) {
                ok = true;
            }
        }

        if(ok) {
            List<Permission> permissions = new ArrayList<>();
            for(String s : editUserRequest.getPermissions()){
                permissions.add(permissionService.findPermission(s));
            }

            return ResponseEntity.ok(
                    userService.editUser(userService.findById(id), editUserRequest.getName(), editUserRequest.getSurname(), editUserRequest.getEmail(),
                            permissions));
        }else{
            return ResponseEntity.status(401).build();
        }


//        List<Permission> permissions = new ArrayList<>();
//        for(String s : editUserRequest.getPermissions()){
//            permissions.add(permissionService.findPermission(s));
//        }
//
//        return ResponseEntity.ok(
//            userService.editUser(userService.findById(id), editUserRequest.getName(), editUserRequest.getSurname(), editUserRequest.getEmail(),
//                        permissions));
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id, @RequestParam String jwt){

        String email = jwtUtil.extractUsername(jwt);
        User loggedUser = userService.findByEmail(email);
        boolean ok = false;

        for(Permission permission: loggedUser.getPermissions()) {
            if (permission.getPermission().equals("can_delete_users")) {
                ok = true;
            }
        }

        if(ok) {
            userService.deleteById(id);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(401).build();
        }

    }

    @GetMapping(value = "/users/edit-user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable("id") Long id, @RequestParam String jwt) {

        String email = jwtUtil.extractUsername(jwt);
        User loggedUser = userService.findByEmail(email);
        boolean ok = false;

        for(Permission permission: loggedUser.getPermissions()) {
            if (permission.getPermission().equals("can_read_users")) {
                ok = true;
            }
        }

        if(ok) {
            User user;
            user = userService.findById(id);
            return ResponseEntity.ok(user);
        }else{
            return ResponseEntity.status(401).build();
        }

    }

}
