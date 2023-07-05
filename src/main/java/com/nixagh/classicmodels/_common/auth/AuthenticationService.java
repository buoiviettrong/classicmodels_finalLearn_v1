package com.nixagh.classicmodels._common.auth;

import com.nixagh.classicmodels.config.JwtService;
import com.nixagh.classicmodels.entity.token.Token;
import com.nixagh.classicmodels.entity.token.TokenType;
import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.exception.AlreadyExistsException;
import com.nixagh.classicmodels.exception.InvalidToken;
import com.nixagh.classicmodels.exception.InvalidUserNameOrPassword;
import com.nixagh.classicmodels.exception.NotFoundEntity;
import com.nixagh.classicmodels.repository.authRepo.RoleRepository;
import com.nixagh.classicmodels.repository.authRepo.TokenRepository;
import com.nixagh.classicmodels.repository.authRepo.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;
    private final RoleRepository roleRepository;

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

        token = jwtService.generateToken(user);
        refreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);

        var saveUser = userRepository.save(user);
        saveUserToken(saveUser, token);
        user.setPassword(null);
        return AuthenticationResponse.builder()
                .userDetails(user)
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

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        user.setPassword(null);
        return AuthenticationResponse.builder()
                .userDetails(user)
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

        var jwtToken = jwtService.generateToken(userDetails);
        refreshToken = jwtService.generateRefreshToken(userDetails, jwtService.extractClaims(refreshToken, Claims::getExpiration));
        saveUserToken(userDetails, jwtToken);

        userRepository.updateRefreshToken(userDetails.getId(), refreshToken);

        userDetails.setPassword(null);
        return AuthenticationResponse.builder()
                .userDetails(userDetails)
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
