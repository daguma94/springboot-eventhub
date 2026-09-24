package com.eventhub.userservice.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eventhub.userservice.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.eventhub.userservice.dto.UserRequest;
import com.eventhub.userservice.dto.UserResponse;
import com.eventhub.userservice.entity.User;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;

import com.eventhub.userservice.exception.EmailAlreadyExistsException;

import java.util.Optional;

import com.eventhub.userservice.exception.UserNotFoundException;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserShouldReturnCreatedUser() {

        UserRequest request = new UserRequest(
            "Daniel",
            "daniel@example.com"
        );

        User savedUser = new User(
            1L,
            "Daniel",
            "daniel@example.com"
        );

        when(userRepository.existsByEmail("daniel@example.com"))
            .thenReturn(false);

        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

        UserResponse response = userService.createUser(request);

        assertEquals(1L, response.id());
        assertEquals("Daniel", response.name());
        assertEquals("daniel@example.com", response.email());

        verify(userRepository).existsByEmail("daniel@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUserShouldThrowExceptionWhenEmailAlreadyExists() {

        UserRequest request = new UserRequest(
            "Daniel",
            "daniel@example.com"
        );

        when(userRepository.existsByEmail("daniel@example.com"))
            .thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(
            EmailAlreadyExistsException.class,
            () -> userService.createUser(request)
        );

        assertEquals(
            "Email already exists: daniel@example.com",
            exception.getMessage()
        );

        verify(userRepository).existsByEmail("daniel@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserByIdShouldReturnUserWhenUserExists() {

        User user = new User(
            1L,
            "Daniel",
            "daniel@example.com"
        );

        when(userRepository.findById(1L))
            .thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertEquals(1L, response.id());
        assertEquals("Daniel", response.name());
        assertEquals("daniel@example.com", response.email());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(99L))
            .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> userService.getUserById(99L)
        );

        assertEquals(
            "User not found with id: 99",
            exception.getMessage()
        );

        verify(userRepository).findById(99L);
    }

    @Test
    void getAllUsersShouldReturnAllUsers() {

        User user1 = new User(
            1L,
            "Daniel",
            "daniel@example.com"
        );

        User user2 = new User(
            2L,
            "Laura",
            "laura@example.com"
        );

        when(userRepository.findAll())
            .thenReturn(List.of(user1, user2));

        List<UserResponse> responses = userService.getAllUsers();

        assertEquals(2, responses.size());

        assertEquals(1L, responses.get(0).id());
        assertEquals("Daniel", responses.get(0).name());
        assertEquals("daniel@example.com", responses.get(0).email());

        assertEquals(2L, responses.get(1).id());
        assertEquals("Laura", responses.get(1).name());
        assertEquals("laura@example.com", responses.get(1).email());

        verify(userRepository).findAll();
    }
}
