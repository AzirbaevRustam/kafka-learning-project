package com.example.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static UserEvent create(String username, String email, String firstName, String lastName) {
        return new UserEvent(
                null,
                username,
                email,
                firstName,
                lastName,
                LocalDateTime.now()
        );
    }
}