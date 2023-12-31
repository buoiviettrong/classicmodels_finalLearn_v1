package com.nixagh.classicmodels.service.auth_service;

import com.nixagh.classicmodels.config.sercurity.JwtService;
import com.nixagh.classicmodels.dto.auth.AuthenticateRequest;
import com.nixagh.classicmodels.dto.auth.AuthenticationResponse;
import com.nixagh.classicmodels.dto.auth.RegisterRequest;
import com.nixagh.classicmodels.entity.auth.Token;
import com.nixagh.classicmodels.entity.auth.User;
import com.nixagh.classicmodels.entity.enums.TokenType;
import com.nixagh.classicmodels.entity.firebase.NotificationMessage;
import com.nixagh.classicmodels.exception.exceptions.AlreadyExistsException;
import com.nixagh.classicmodels.exception.exceptions.InvalidToken;
import com.nixagh.classicmodels.exception.exceptions.InvalidUserNameOrPassword;
import com.nixagh.classicmodels.exception.exceptions.NotFoundEntity;
import com.nixagh.classicmodels.repository.role.RoleRepository;
import com.nixagh.classicmodels.repository.token.TokenRepository;
import com.nixagh.classicmodels.repository.user.UserRepository;
import com.nixagh.classicmodels.service.web_socket_service.IWebSocketService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final IWebSocketService webSocketService;

    public AuthenticationResponse register(RegisterRequest request) {
        String token;
        String refreshToken;

//        check email
        userRepository.checkEmail(request.getEmail())
                .ifPresent((user) -> {
                    throw new AlreadyExistsException("Email %s already exists".formatted(user.getEmail()));
                });

        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() == null ? roleRepository.getRoleByRoleName("MANAGER") : request.getRole())
                .loginType(request.getType())
                .build();

        token = jwtService.generateToken(jwtService.generateClaims(user), user);
        refreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);

        var saveUser = userRepository.save(user);
        saveUserToken(saveUser, token);
        user.setPassword(null);

        String result = webSocketService.sendNotification(
                NotificationMessage.builder()
                        .title("New user register")
                        .body("New user register with email: %s".formatted(user.getEmail()))
                        .data(Map.of(
                                "email", saveUser.getEmail(),
                                "id", saveUser.getId().toString()
                                )
                        )
                .build(),
                "/admin/notification");

        return AuthenticationResponse.builder()
                .userDetails(null)
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        Authentication authentication;
        // kiem tra dang nhap
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new InvalidUserNameOrPassword("Wrong username or password");
        }

        User user = (User) authentication.getPrincipal();

        var jwtToken = jwtService.generateToken(jwtService.generateClaims(user), user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        user.setPassword(null);
        return AuthenticationResponse.builder()
                .userDetails(null)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refresh(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        User userDetails = this.userRepository.getUserByEmail(username).orElseThrow(() -> new NotFoundEntity("User not found"));

        // kiem tra khop voi trong database
        if (!userDetails.getRefreshToken().equals(refreshToken))
            throw new InvalidToken("Refresh token is invalid");

        // kiem tra thoi gian token
        if (!jwtService.isTokenValid(refreshToken, userDetails))
            throw new InvalidToken("Refresh token is invalid");

        revokeAllUserTokens(userDetails);

        var jwtToken = jwtService.generateToken(jwtService.generateClaims(userDetails), userDetails);
        refreshToken = jwtService.generateRefreshToken(userDetails, jwtService.extractClaims(refreshToken, Claims::getExpiration));
        saveUserToken(userDetails, jwtToken);

        userRepository.updateRefreshToken(userDetails.getId(), refreshToken);

        userDetails.setPassword(null);
        return AuthenticationResponse.builder()
                .userDetails(null)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User saveUser, String jwtToken) {
        Token token = Token.builder()
                .accessToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .user(saveUser)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        tokenRepository.revokeAllUserTokens(user);
    }
}
