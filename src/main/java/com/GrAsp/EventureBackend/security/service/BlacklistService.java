package com.GrAsp.EventureBackend.security.service;

import com.GrAsp.EventureBackend.security.repository.RedisTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class BlacklistService {
    private final String REDIS_BLACKLIST_KEY = "eventure_blacklist_token:";
    private final RedisTokenRepository redisTokenRepository;

    public BlacklistService(RedisTokenRepository redisTokenRepository) {
        this.redisTokenRepository = redisTokenRepository;
    }


    public void blacklistToken(String token, String expiredAt) {
        Duration duration = Duration.between(java.time.Instant.now(), java.time.Instant.parse(expiredAt));
        redisTokenRepository.saveToken(REDIS_BLACKLIST_KEY + token, duration);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTokenRepository.isTokenBlacklisted(REDIS_BLACKLIST_KEY + token);
    }
}
