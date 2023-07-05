package com.nixagh.classicmodels._common.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nixagh.classicmodels.entity.auth.Role;
import com.nixagh.classicmodels.entity.user.LoginType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
