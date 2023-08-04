package com.nixagh.classicmodels.service.auth_service;

import com.nixagh.classicmodels.config.sercurity.JwtService;
import com.nixagh.classicmodels.dto.auth.AuthenticateRequest;
import com.nixagh.classicmodels.dto.auth.AuthenticationResponse;
import com.nixagh.classicmodels.entity.auth.Token;
import com.nixagh.classicmodels.entity.auth.User;
import com.nixagh.classicmodels.entity.enums.TokenType;
import com.nixagh.classicmodels.entity.firebase.NotificationMessage;
import com.nixagh.classicmodels.exception.exceptions.InvalidUserNameOrPassword;
import com.nixagh.classicmodels.repository.token.TokenRepository;
import com.nixagh.classicmodels.service.web_socket_service.IWebSocketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService{

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IWebSocketService webSocketService;

    @Override
    public AuthenticationResponse authenticateOnlyOneDevice(AuthenticateRequest request, HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getLocalAddr();
        String device = httpServletRequest.getHeader("User-Agent");

        System.out.println("ip: " + ip);
        System.out.println("device: " + device);

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

        var jwtToken = jwtService.generateToken(jwtService.generateClaims(user, ip, device), user);
        var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);

        // check token exist in db with ip and device
        long result = tokenRepository.checkTokenExistWithIpNotEqualORDeviceNotEqual(user, ip, device);

        if(result > 0) {
            webSocketService.sendNotificationToUser(
                    NotificationMessage.builder()
                            .title("Thông báo")
                            .body("Tài khoản của bạn đã đăng nhập trên một thiết bị khác")
                            .data(Map.of("id", user.getId().toString()))
                            .build(),
                    user.getUsername(),
                    ip
            );
        }

        saveUserTokenWithIpAndDevice(user, jwtToken, ip, device);
        user.setPassword(null);
        return AuthenticationResponse.builder()
                .userDetails(null)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();


    }

    private void saveUserTokenWithIpAndDevice(User user, String jwtToken, String ip, String device) {
        final Token token = Token.builder()
                .accessToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .user(user)
                .device(device)
                .ip(ip)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}
