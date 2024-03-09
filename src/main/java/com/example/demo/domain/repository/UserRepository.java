package com.example.demo.domain.repository;

import com.example.demo.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<Long> create(User user);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByCpfOrEmail(String cpf, String email);
    List<User> findAllUsers();
    void updateUser(User user);
    void deleteUser(Long userId);
}
