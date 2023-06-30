package com.nixagh.classicmodels._common.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nixagh.classicmodels.entity.auth.Role;
import com.nixagh.classicmodels.entity.user.LoginType;
import com.nixagh.classicmodels.repository.authRepo.RoleRepository;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @JsonIgnore
    private Role role;
    @JsonIgnore
    private LoginType type = LoginType.NORMAL;
}
