package org.example.jwt_cookie_study.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import javax.crypto.SecretKey;
import org.example.jwt_cookie_study.config.CustomUserDetail;
import org.example.jwt_cookie_study.domain.User;
import org.example.jwt_cookie_study.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final Key key;
    private final long ACCESS_TOKEN_VALID_TIME = Duration.ofSeconds(10).toMillis();
    private final long REFRESH_TOKEN_VALID_TIME = Duration.ofDays(7).toMillis();

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateToken(Authentication authentication) {

        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String email = userDetail.getUsername();
        long userId = userDetail.getUserId();
        Collection<? extends GrantedAuthority> authorities = userDetail.getAuthorities();
        Date now = new Date();

        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim("email", email)
            .claim("userId", userId)
            .claim("type", "acT")
            .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        String refreshToken = Jwts.builder()
            .claim("email", email)
            .claim("userId", userId)
            .claim("type", "reT")
            .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        TokenDto token = TokenDto
            .builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();

        for (GrantedAuthority authority : authorities) {
            token.setRole(authority.getAuthority());
        }

        return token;
    }

    public String accessTokenReissuance(User user) {
        Date now = new Date();

        return Jwts.builder()
            .claim("email", user.getEmail())
            .claim("userId", user.getUserId())
            .claim("type", "acT")
            .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }


    public Claims parseClaims(String accessToken) {
        return Jwts.parser().verifyWith((SecretKey) key).build()
            .parseSignedClaims(accessToken).getPayload(); // 만료 예외는 던지게 둠
    }

    public String validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
            return "Success";
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            return "Invalid";
        } catch (ExpiredJwtException e) {
            return "Expired";
        } catch (UnsupportedJwtException e) {
            return "Unsupported";
        } catch (IllegalArgumentException e) {
            return "JWT claims string is empty.";
        }

    }

    public Long getUserIdFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)           // JwtProvider의 서명키
            .build()
            .parseClaimsJws(refreshToken) // 서명/만료 검증 수행
            .getBody();

        return claims.get("userId", Number.class).longValue(); // ✅ 핵심
    }
}
