package org.kir.service.cache;

import com.kir.commonservice.constant.CacheConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int DEFAULT_TTL = 30 * 60; // 30 mins

    public void cacheUserRoles(Long userId, Set<String> roles){
        log.debug("user id {} roles {}", userId, roles);
        String key = CacheConstant.CacheKeys.userRolesKey(userId);
        redisTemplate.opsForValue().set(key, roles, DEFAULT_TTL, TimeUnit.SECONDS);
        log.debug("Cached user roles for userId: {}", userId);
    }


    public Set<String> getUserRoles(Long userId){
        String key = CacheConstant.CacheKeys.userRolesKey(userId);
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Set) {
            return (Set<String>) cached;
        }
        return null;
    }

    public void cacheUserPermissions(Long userId, Set<String> permissions){
        String key = CacheConstant.CacheKeys.userPermissionsKey(userId);
        redisTemplate.opsForValue().set(key, permissions, DEFAULT_TTL, TimeUnit.SECONDS);
        log.debug("Cached user permissions for userId: {}", userId);
    }

    public Set<String> getUserPermissions(Long userId){
        String key = CacheConstant.CacheKeys.userPermissionsKey(userId);
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Set) {
            return (Set<String>) cached;
        }
        return null;
    }


    public void invalidateUserCache(Long userId){
        redisTemplate.delete(CacheConstant.CacheKeys.userRolesKey(userId));
        redisTemplate.delete(CacheConstant.CacheKeys.userPermissionsKey(userId));
        log.debug("Cached user roles for userId: {}", userId);
    }

    public void invalidateAllUserCache(){
        Set<String> keys = redisTemplate.keys(CacheConstant.CacheKeys.USER_ROLES + ":*");
        keys.addAll(redisTemplate.keys(CacheConstant.CacheKeys.USER_PERMISSIONS + ":*"));
        if(!keys.isEmpty()){
            redisTemplate.delete(keys);
            log.info("Invalidated all user caches, count: {}", keys.size());
        }
    }

}
