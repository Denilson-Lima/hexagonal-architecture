package com.example.demo.application.dto.request;

import com.example.demo.domain.model.User;
import lombok.Getter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
public class UserRequest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String name;
    private String dateOfBirth;
    private String cpf;
    private String email;

    public static User fromDto(UserRequest userRequest) {
        LocalDate dateOfBirth = LocalDate.parse(userRequest.dateOfBirth, formatter);
        return new User(null, userRequest.name, dateOfBirth, userRequest.cpf, userRequest.email);
    }
}
