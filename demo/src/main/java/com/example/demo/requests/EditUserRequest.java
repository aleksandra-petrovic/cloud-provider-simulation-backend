package com.example.demo.requests;

import com.example.demo.model.Permission;
import lombok.Data;

import java.util.List;

@Data
public class EditUserRequest {

    private String name;

    private String surname;

    private String email;

    private List<String> permissions;

}
