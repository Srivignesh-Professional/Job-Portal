package com.jobportal.findworks.dto.auth;
import com.jobportal.findworks.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    @NotNull
    private User.Role role;
}