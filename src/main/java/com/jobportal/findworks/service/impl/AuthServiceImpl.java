package com.jobportal.findworks.service.impl;

import com.jobportal.findworks.dto.auth.RegisterForm;
import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.service.AuthService;
import com.jobportal.findworks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    public User register(RegisterForm form) {
        // Later we can normalize phone format here (country code etc.)
        return userService.createUser(form.getPhone(), form.getPassword(), form.getRole());
    }
}
