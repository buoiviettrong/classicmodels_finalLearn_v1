package com.nixagh.classicmodels.entity.auth;


import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class AuthSettings {
    @Id
    private String settingsId;

    private String JwtSecretKey;
    private long JwtExpiration;
    private long JwtRefreshTokenExpiration;

    @Enumerated(EnumType.STRING)
    private SignatureAlgorithm signatureAlgorithm;

    @OneToMany(mappedBy = "authSettings")
    private List<OAuthClient> clients = new ArrayList<OAuthClient>();

}
