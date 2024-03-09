package com.example.demo.domain.usecase;

import com.example.demo.domain.exception.EmptyUpdateFieldsException;
import com.example.demo.domain.exception.UserAlreadyExistsException;
import com.example.demo.domain.exception.UserNotFoundException;
import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class UserUseCase {

    private final UserRepository userRepository;

    @Autowired
    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long save(User user) {
        var userExists = findUserByCpfOrEmail(user.getCpf(), user.getEmail());

        if(Objects.nonNull(userExists)) {
            throw new UserAlreadyExistsException("User Already Exists");
        }

        var id = this.userRepository.create(user);

        return id.orElse(null);
    }

    public User findUserById(Long id) {
        return this.userRepository.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }

    public User findUserByCpfOrEmail(String cpf, String email) {
        return this.userRepository.getUserByCpfOrEmail(cpf, email)
                .orElse(null);
    }

    public List<User> findAllUser() {
        return this.userRepository.findAllUsers();
    }

    public void updateUser(Long id, String name, String dateOfBitch, String email) {

        if (Objects.isNull(name) && Objects.isNull(dateOfBitch) && Objects.isNull(email)) {
            throw new EmptyUpdateFieldsException("At least one field must be provided for update");
        }

        var userExists = this.findUserById(id);

        boolean shouldUpdate = false;
        if (Objects.nonNull(name) && !name.equals(userExists.getName())) {
            userExists.setName(name);
            shouldUpdate = true;
        }

        if (Objects.nonNull(email) && !email.equals(userExists.getEmail())) {
            userExists.setEmail(email);
            shouldUpdate = true;
        }

        if (Objects.nonNull(dateOfBitch)) {
            var formattedDate = LocalDate.parse(dateOfBitch, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (!formattedDate.equals(userExists.getDateOfBirth())) {
                userExists.setDateOfBirth(formattedDate);
                shouldUpdate = true;
            }
        }

        if (shouldUpdate) {
            userRepository.updateUser(userExists);
        }
    }

    public void deleteUser(Long id) {
        this.findUserById(id);
        this.userRepository.deleteUser(id);
    }
}
