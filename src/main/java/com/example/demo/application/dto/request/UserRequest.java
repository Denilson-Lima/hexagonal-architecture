package com.example.demo.application.dto.request;

import com.example.demo.domain.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class UserRequest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @NotBlank(message = "name cannot be null or empty")
    private String name;

    @NotBlank(message = "dateOfBirth cannot be null or empty")
    private String dateOfBirth;

    @NotBlank(message = "cpf cannot be null or empty")
    private String cpf;

    @Email(message = "provide a valid email address")
    @NotBlank(message = "email cannot be null or empty")
    private String email;

    public static User fromDto(UserRequest userRequest) {
        LocalDate dateOfBirth = LocalDate.parse(userRequest.dateOfBirth, formatter);
        return new User(null, userRequest.name, dateOfBirth, userRequest.cpf, userRequest.email);
    }
}
