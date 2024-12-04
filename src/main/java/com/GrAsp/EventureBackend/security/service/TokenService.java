package com.GrAsp.EventureBackend.security.service;

import com.GrAsp.EventureBackend.model.User;
import com.GrAsp.EventureBackend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final UserRepository usersRepository;

    public TokenService(JwtEncoder jwtEncoder, UserRepository usersRepository) {
        this.jwtEncoder = jwtEncoder;
        this.usersRepository = usersRepository;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;

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
                .build();
// If you want to use shared secret, use line below
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        // If you want to use RSA Key Pair, use line below
//        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
