package com.nixagh.classicmodels._common.auth;

import com.nixagh.classicmodels.config.JwtService;
import com.nixagh.classicmodels.entity.token.Token;
import com.nixagh.classicmodels.entity.token.TokenType;
import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.repository.TokenRepository;
import com.nixagh.classicmodels.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        String token;
        String refreshToken;

        //check email
//        User existUser = userRepository.getUserByEmail(request.getEmail()).orElse(null);
//        if (existUser == null) return null;

        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .type(request.getType())
                .build();

        var saveUser = userRepository.save(user);
        token = jwtService.generateToken(user);
        refreshToken =  jwtService.generateRefreshToken(user);

        saveUserToken(saveUser, token);

        return AuthenticationResponse.builder()
                .email(request.getEmail())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticateRequest request) {

        // kiem tra dang nhap
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.getUserByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .email(request.getEmail())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    private void saveUserToken(User saveUser, String jwtToken) {
        Token token = Token.builder()
                .token_(jwtToken)
                .tokenType(TokenType.BEARER)
                .user(saveUser)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
