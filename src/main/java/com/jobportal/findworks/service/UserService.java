package com.jobportal.findworks.service;

import com.jobportal.findworks.entity.User;

import java.util.Optional;

public interface UserService {
    User createUser(String phone, String rawPassword, User.Role role);
    Optional<User> findByPhone(String phone);
}