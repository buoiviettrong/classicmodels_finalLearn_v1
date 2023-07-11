package com.nixagh.classicmodels._common.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nixagh.classicmodels.entity.user.User;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationResponse implements Serializable {
    @JsonProperty("user")
    private User userDetails;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("redirect")
    private String redirect;
}
