package com.nixagh.classicmodels._common.auth;

import com.nixagh.classicmodels.entity.user.LoginType;
import com.nixagh.classicmodels.entity.user.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role = Role.USER;
    private LoginType type = LoginType.NORMAL;
}
