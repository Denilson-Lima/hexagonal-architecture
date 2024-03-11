package com.example.demo.datasource.repository;

import com.example.demo.builder.UserBuilder;
import com.example.demo.configuration.PostgresTestContainer;
import com.example.demo.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[DATASOURCE] - UserRepositoryPostgresTest")
public class UserRepositoryPostgresTest {

    @Container
    private static final PostgresTestContainer postgresTestContainer = PostgresTestContainer.getInstance();

    private static UserRepository userRepository;

    @BeforeAll
    static void setup() {
        if (!postgresTestContainer.isRunning()) {
            postgresTestContainer.start();
        }
        userRepository = new UserRepositoryPostgres(new NamedParameterJdbcTemplate(PostgresTestContainer.dataSource()));
    }

    @BeforeAll
    static void tearDown() {
        postgresTestContainer.stop();
    }

    @Test
    @DisplayName("[CREATE] - Save new User")
    void test_1() {
        var newUser = UserBuilder.defaultUser().build();
        var userId = userRepository.create(newUser);

        assertThat(userId).isPresent().isNotNull();
    }

    @Test
    @DisplayName("[READ] - Get user by id")
    void test_2() {
        var newUser = UserBuilder.defaultUser().build();
        var userId = userRepository.create(newUser).orElse(null);

        assertThat(userId).isNotNull();

        var userExists = userRepository.getUserById(userId);
        assertThat(userExists).isPresent().isNotNull();
        assertThat(newUser.getCpf()).isEqualTo(userExists.get().getCpf());
    }

    @Test
    @DisplayName("[READ] - Get all users")
    void test_3() {
        var user_1 = UserBuilder.defaultUser().build();
        var user_2 = UserBuilder.defaultUser().build();

        userRepository.create(user_1);
        userRepository.create(user_2);

        var listUser = userRepository.findAllUsers();
        assertThat(listUser).isNotEmpty();
        assertThat(listUser).contains(user_1, user_2);
    }

    @Test
    @DisplayName("[UPDATE] - Update user")
    void test_4() {
        var newUser = UserBuilder.defaultUser().build();
        var userId = userRepository.create(newUser).orElse(null);

        assertThat(userId).isNotNull();

        var email = "test@test.com";
        var name = "test123";
        var dateOfBirth = LocalDate.parse("2000-01-01");

        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setDateOfBirth(dateOfBirth);

        userRepository.updateUser(newUser);

        var findUser = userRepository.getUserById(userId).orElse(null);

        assertThat(findUser).isNotNull();
        assertThat(findUser.getEmail()).isEqualTo(email);
        assertThat(findUser.getName()).isEqualTo(name);
        assertThat(findUser.getDateOfBirth()).isEqualTo(dateOfBirth);
    }

    @Test
    @DisplayName("[DELETE] - Delete user by id")
    void test_5() {
        var newUser = UserBuilder.defaultUser().build();
        var userId = userRepository.create(newUser).orElse(null);
        assertThat(userId).isNotNull();

        userRepository.deleteUser(userId);

        var findUser = userRepository.getUserById(userId);
        assertThat(findUser).isEmpty();
    }
}
