package com.flyemirates.service;

import com.flyemirates.dto.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    void deleteUser(Long id);
    UserResponse updateUserRole(Long id, String roleName);
}