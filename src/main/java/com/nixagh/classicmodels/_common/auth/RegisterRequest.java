package com.nixagh.classicmodels._common.auth;

import com.nixagh.classicmodels.entity.auth.Role;
import com.nixagh.classicmodels.entity.enums.Provider;
import com.nixagh.classicmodels.entity.user.LoginType;
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
    private Role role = null;
    private LoginType type = LoginType.NORMAL;
}
