package com.example.demo.controllers;

import com.example.demo.model.Machine;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.requests.CreateRequest;
import com.example.demo.requests.ScheduleRequest;
import com.example.demo.requests.SearchRequest;
import com.example.demo.requests.UpdateRequest;
import com.example.demo.services.MachineService;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtil;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/machines")
public class MachineController {

    private final MachineService machineService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public MachineController(MachineService machineService, UserService userService, JwtUtil jwtUtil){
        this.machineService = machineService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    public String takeJwt(String jwt){
        if(jwt != null && jwt.startsWith("Bearer ")) return jwt.substring(7);
        else return "";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMachine(@Valid @RequestBody CreateRequest createRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) throws ParseException {
        jwt = takeJwt(jwt);
        String email = jwtUtil.extractUsername(jwt);

        if(!jwtUtil.isTokenExpired(jwt) &&
                jwtUtil.extractPermission(jwt,"can_create_machines").equals(true)){
            return ResponseEntity.ok(machineService.create(this.userService.findByEmail(email), createRequest.getMachineName()));
        }else{
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/destroy/{id}")
    public ResponseEntity<?> destroyMachine(@PathVariable("id") Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        jwt = takeJwt(jwt);

        if(!jwtUtil.isTokenExpired(jwt) &&
                jwtUtil.extractPermission(jwt,"can_destroy_machines").equals(true)){
            this.machineService.destroy(id);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchMachines(@RequestBody SearchRequest searchRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        jwt = takeJwt(jwt);
        User loggedUser = userService.findByEmail(jwtUtil.extractUsername(jwt));

        if (!jwtUtil.isTokenExpired(jwt) &&
                jwtUtil.extractPermission(jwt, "can_search_machines").equals(true)) {

            return ResponseEntity.ok(machineService.search(loggedUser, searchRequest.getMachineName(),
                        searchRequest.getStatus(), searchRequest.getDateFrom(),
                        searchRequest.getDateTo()));
        }else{
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> destroyMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        jwt = takeJwt(jwt);
        User loggedUser = userService.findByEmail(jwtUtil.extractUsername(jwt));

        if(!jwtUtil.isTokenExpired(jwt)){
//            this.machineService.getErrors(loggedUser);
            return ResponseEntity.ok(this.machineService.getErrors(loggedUser));
        }else{
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateMachine(@PathVariable("id") Long id, @RequestBody UpdateRequest updateRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) throws ParseException {
        jwt = takeJwt(jwt);

        if (!jwtUtil.isTokenExpired(jwt) &&
                jwtUtil.extractPermission(jwt, "can_" + updateRequest.getOperation() + "_machines").equals(true)) {
            if(updateRequest.getDate() == null) {
                machineService.update(id, updateRequest.getOperation());
            }else {
                machineService.scheduledUpdate(id, updateRequest.getOperation(), updateRequest.getDate());
            }
            return ResponseEntity.ok( "{\"response\":\"machine starting...\"}");
        }else{
            return ResponseEntity.status(401).build();
        }
    }
}
