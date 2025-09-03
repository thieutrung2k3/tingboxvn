package org.kir.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    private final String PREFIX = "blacklist_jti:";

    @Value("${jwt.refresh-duration}")
    private long refreshDuration;

    public void cacheBlacklistToken(String jti) {
        String key = PREFIX + jti;
        redisTemplate.opsForValue().set(key, "revoked", refreshDuration, TimeUnit.SECONDS);
    }

    public boolean isBlacklisted(String jti) {
        String key = PREFIX + jti;
        return redisTemplate.hasKey(key);
    }
}
