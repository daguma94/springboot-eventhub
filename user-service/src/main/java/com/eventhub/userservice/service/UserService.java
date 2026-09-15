package com.eventhub.userservice.service;

import java.util.List;

import com.eventhub.userservice.dto.UserRequest;
import com.eventhub.userservice.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);
}
