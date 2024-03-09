package com.example.demo.application.dto.request;

import lombok.Getter;

@Getter
public class UserPatchRequest {
    private String name;
    private String dateOfBirth;
    private String email;
}
