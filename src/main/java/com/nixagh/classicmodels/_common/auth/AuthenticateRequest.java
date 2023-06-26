package com.nixagh.classicmodels._common.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticateRequest {
  private String email;
  private String password;
}
