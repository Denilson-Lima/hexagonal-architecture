package com.example.demo.builder;

import com.example.demo.domain.model.User;
import java.time.LocalDate;
import java.util.UUID;

public class UserBuilder {

    private User user;

    public static UserBuilder defaultUser() {
        var builder = new UserBuilder();
        builder.user = User.builder()
                .name("denilson")
                .dateOfBirth(LocalDate.parse("2000-01-01"))
                .email(UUID.randomUUID() + "@test.com")
                .cpf(String.valueOf(UUID.randomUUID()).substring(0, 11))
                .build();
        return builder;
    }

    public UserBuilder withName(String name) {
        this.user.setName(name);
        return this;
    }

    public UserBuilder withDateOfBirth(LocalDate dateOfBirth) {
        this.user.setDateOfBirth(dateOfBirth);
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.user.setEmail(email);
        return this;
    }

    public User build() {
        return this.user;
    }
}
