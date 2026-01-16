package com.example.producer.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}