package com.example.demo.domain.usecase;

import com.example.demo.builder.UserBuilder;
import com.example.demo.domain.exception.EmptyUpdateFieldsException;
import com.example.demo.domain.exception.UserAlreadyExistsException;
import com.example.demo.domain.exception.UserNotFoundException;
import com.example.demo.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("[DOMAIN] - Unit Test For UserUseCase")
public class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    @DisplayName("[CREATE] - Save User")
    void test_1() {
        var user = UserBuilder.defaultUser().build();

        doReturn(null).when(userUseCase).findUserByCpfOrEmail(user.getCpf(), user.getEmail());
        when(userRepository.create(user)).thenReturn(Optional.of(1L));

        var result = userUseCase.save(user);
        assertThat(result).isNotNull().isEqualTo(1L);
    }

    @Test
    @Order(2)
    @DisplayName("[CREATE] - Should fail when saving duplicate user")
    void test_2() {
        var user = UserBuilder.defaultUser().build();
        when(userUseCase.findUserByCpfOrEmail(user.getCpf(), user.getEmail())).thenReturn(user);
        assertThatThrownBy(() -> userUseCase.save(user)).isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    @Order(3)
    @DisplayName("[READ] - Find User By Id")
    void test_3() {
        var user = UserBuilder.defaultUser().build();
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));

        var result = userUseCase.findUserById(1L);
        assertThat(result).isNotNull().isEqualTo(user);
    }

    @Test
    @Order(4)
    @DisplayName("[READ] - Should fail when user not found")
    void test_4() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userUseCase.findUserById(1L)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @Order(5)
    @DisplayName("[READ] - Find User By Cpf Or Email")
    void test_5() {
        var user = UserBuilder.defaultUser().build();
        when(userRepository.getUserByCpfOrEmail(user.getCpf(), user.getEmail())).thenReturn(Optional.of(user));

        var result = userUseCase.findUserByCpfOrEmail(user.getCpf(), user.getEmail());
        assertThat(result).isNotNull().isEqualTo(user);
    }

    @Test
    @Order(6)
    @DisplayName("[READ] - Should return empty list when users not found")
    void test_6() {
        when(userRepository.getUserByCpfOrEmail(any(), any())).thenReturn(Optional.empty());
        var result = userUseCase.findUserByCpfOrEmail(any(), any());
        assertThat(result).isNull();
    }

    @Test
    @Order(7)
    @DisplayName("[READ] - Find All User")
    void test_7() {
        when(userRepository.findAllUsers()).thenReturn(List.of());
        var result = userUseCase.findAllUser();
        assertThat(result).isNotNull();
    }

    @Test
    @Order(8)
    @DisplayName("[UPDATE] - Update User")
    void test_8() {
        var user = UserBuilder.defaultUser().withId(1L).build();

        doReturn(user).when(userUseCase).findUserById(user.getId());
        doNothing().when(userRepository).updateUser(user);

        userUseCase.updateUser(user.getId(), "New Name", "01/01/2024", "New Email");

        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    @Order(9)
    @DisplayName("[UPDATE] - Should fail when all fields are null")
    void test_9() {
        assertThatThrownBy(() -> userUseCase.updateUser(1L, null, null, null))
                .isInstanceOf(EmptyUpdateFieldsException.class);
    }

    @Test
    @Order(10)
    @DisplayName("[DELETE] - Delete User")
    void test_10() {
        var user = UserBuilder.defaultUser().withId(1L).build();
        doReturn(user).when(userUseCase).findUserById(user.getId());
        userUseCase.deleteUser(user.getId());
        verify(userRepository, times(1)).deleteUser(1L);
    }
}
