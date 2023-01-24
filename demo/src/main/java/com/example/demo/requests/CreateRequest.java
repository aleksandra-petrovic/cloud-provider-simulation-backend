package com.example.demo.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateRequest {
    @NotBlank(message = "Name is mandatory.")
    private String machineName;
}
