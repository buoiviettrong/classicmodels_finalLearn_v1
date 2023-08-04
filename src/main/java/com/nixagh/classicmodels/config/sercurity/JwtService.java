package com.nixagh.classicmodels.config.sercurity;

import com.nixagh.classicmodels.entity.auth.AuthSettings;
import com.nixagh.classicmodels.entity.auth.Permission;
import com.nixagh.classicmodels.entity.auth.User;
import com.nixagh.classicmodels.exception.exceptions.InvalidToken;
import com.nixagh.classicmodels.exception.exceptions.NotFoundEntity;
import com.nixagh.classicmodels.exception.exceptions.SignatureTokenException;
import com.nixagh.classicmodels.repository.auth_setting.SettingRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
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

    // get authorities from token
    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        List<String> authorities = (List<String>) claims.get("authorities");
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }

    public Map<String, Object> generateClaims(User user) {
        Map<String, Object> claims = new java.util.HashMap<>(Map.of("role", user.getRole().getRoleName()));
        // add email to claims
        claims.put("email", user.getEmail());
        // add permission to claims
        claims.put("permission", user.getRole().getPermissions().stream().map(Permission::getPermissionName).toList());
        // add customer id to claims
        claims.put("customerNumber", user.getCustomerNumber());
        // add user id to claims
        claims.put("userId", user.getId());
        // add user type to claims
        claims.put("userType", user.getLoginType());
        //add username to claims
        claims.put("userName", user.getFirstName() + " " + user.getLastName());
        return claims;
    }

    public Map<String, Object> generateClaims(User user, String ip, String userAgent) {
        Map<String, Object> claims = generateClaims(user);
        // add ip to claims
        claims.put("ip", ip);
        // add user agent to claims
        claims.put("userAgent", userAgent);
        return claims;
    }

    public String getRoleFromToken(String accessToken) {
        return extractClaims(accessToken, claims -> claims.get("role", String.class));
    }
}
