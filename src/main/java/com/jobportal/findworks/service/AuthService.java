package com.jobportal.findworks.service;

import com.jobportal.findworks.dto.auth.RegisterForm;
import com.jobportal.findworks.entity.User;

public interface AuthService {
    User register(RegisterForm form);
}
