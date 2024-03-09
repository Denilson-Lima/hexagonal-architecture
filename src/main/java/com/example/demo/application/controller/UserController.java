package com.example.demo.application.controller;

import com.example.demo.application.dto.request.UserPatchRequest;
import com.example.demo.application.dto.request.UserRequest;
import com.example.demo.domain.model.User;
import com.example.demo.domain.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/v1/users/")
public class UserController {

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        return ResponseEntity.ok().body(userUseCase.findAllUser());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userUseCase.findUserById(id));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody UserRequest userRequest) {
        var savedUser = userUseCase.save(UserRequest.fromDto(userRequest));
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser).toUri();
        return ResponseEntity.created(location).body(savedUser);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserPatchRequest u) {
        userUseCase.updateUser(id, u.getName(), u.getDateOfBirth(), u.getEmail());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
