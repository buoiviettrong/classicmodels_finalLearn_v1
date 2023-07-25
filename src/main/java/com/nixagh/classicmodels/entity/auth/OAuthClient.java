package com.nixagh.classicmodels.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oauth_client")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthClient {
    @Id
    private String clientId;
    private String clientName;
    private String clientSecret;

    @ManyToOne
    @JoinColumn(name = "settingsId")
    @JsonIgnore
    private AuthSettings authSettings;
}
