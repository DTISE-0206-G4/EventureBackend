package com.GrAsp.EventureBackend.security.service;

import com.GrAsp.EventureBackend.model.User;
import com.GrAsp.EventureBackend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    public enum TokenType {
        ACCESS, REFRESH
    }
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserRepository usersRepository;
    private final long ACCESS_TOKEN_EXPIRY = 900L; // 15 minutes
    private final long REFRESH_TOKEN_EXPIRY = 86400L; // 24 hours

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserRepository usersRepository) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.usersRepository = usersRepository;
    }

    public String generateToken(Authentication authentication, TokenType tokenType) {
        Instant now = Instant.now();
        long expiry = (tokenType == TokenType.ACCESS) ? ACCESS_TOKEN_EXPIRY : REFRESH_TOKEN_EXPIRY;

        String email = authentication.getName();

        User user = usersRepository.findByEmailContainsIgnoreCase(email).orElseThrow(() -> new RuntimeException("User not found"));

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((a, b) -> a + " " + b)
                .orElse("");

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(email)
                .claim("scope", scope)
                .claim("userId", user.getId())
                .claim("type", tokenType.name())
                .build();
// If you want to use shared secret, use line below
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        // If you want to use RSA Key Pair, use line below
//        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String refreshAccessToken(String refreshToken) {
        Jwt jwt = this.jwtDecoder.decode(refreshToken);
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ACCESS_TOKEN_EXPIRY))
                .subject(jwt.getSubject())
                .claim("scope", jwt.getClaimAsString("scope"))
                .claim("userId", jwt.getClaimAsString("userId"))
                .claim("type", TokenType.ACCESS.name())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
