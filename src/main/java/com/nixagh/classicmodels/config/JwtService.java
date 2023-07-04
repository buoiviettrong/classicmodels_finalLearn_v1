package com.nixagh.classicmodels.config;

import com.nixagh.classicmodels._common.settings.AuthSettings;
import com.nixagh.classicmodels.exception.InvalidToken;
import com.nixagh.classicmodels.exception.NotFoundEntity;
import com.nixagh.classicmodels.exception.SignatureTokenException;
import com.nixagh.classicmodels.repository.authRepo.SettingRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final AuthSettings settings;
    public JwtService(SettingRepository settingRepository) {
        this.settings = settingRepository.getAuthSettings().orElseThrow(() -> new NotFoundEntity("AuthSettings not found"));
    }
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimFunction) {
        final Claims claims = extractAllClaims(token);
        return claimFunction.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, settings.getJwtRefreshTokenExpiration());
    }
    public String generateRefreshToken(
            UserDetails userDetails,
            Date refreshTokenExpiration
    ) {
        return buildToken(new HashMap<>(), userDetails,refreshTokenExpiration);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, settings.getJwtExpiration());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), settings.getSignatureAlgorithm())
                .compact();
    }
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            Date expirationNow
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationNow)
                .signWith(getSignInKey(), settings.getSignatureAlgorithm())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).after(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureTokenException e) {
            throw new SignatureTokenException("JWT signature does not match locally computed signature");
        } catch (ExpiredJwtException e) {
            throw new InvalidToken("JWT expired");
        } catch (Exception e) {
            throw new InvalidToken("JWT token invalid");
        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(settings.getJwtSecretKey());
        return Keys.hmacShaKeyFor(bytes);
    }
}
