package com.zans.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zans.config.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoginCacheHelper {

    /**
     * 登录，连续失败缓存
     */
    Cache<String, Integer> loginAttemptCache;

    /**
     * 登录，锁定缓存
     */
    Cache<String, Object> loginLockCache;

    Cache<String, String> logoutLockCache;



    @PostConstruct
    private void init() {
        log.info("Login Cache init");
        loginAttemptCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.DAYS)
                .maximumSize(10_000)
                .build();

        loginLockCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();

        logoutLockCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.DAYS)
                .maximumSize(10_000)
                .build();

    }


    public void increaseLoginAttemptError(String key) {
        if (key == null) {
            return;
        }
        Integer value = loginAttemptCache.getIfPresent(key);
        if (value == null) {
            value = 0;
        } else {
            value += 1;
        }
        if (value > GlobalConstants.LOGIN_MAX_ATTEMPT) {
            if (loginLockCache.getIfPresent(key) == null) {
                loginLockCache.put(key, value);
            }
        }
        log.info("loginAttemptCache#{}, {}, loginLockCache#{}", key, value, loginLockCache.getIfPresent(key));
        loginAttemptCache.put(key, value);
    }

    public void removeLoginAttemptError(String key) {
        if (key == null) {
            return;
        }
        loginAttemptCache.invalidate(key);
        loginLockCache.invalidate(key);
    }

    public boolean isLoginLocked(String key) {
        if (key == null) {
            return false;
        }
        return this.loginLockCache.getIfPresent(key) != null;
    }

    /**
     * token 的黑名单
     * @param token
     */
    public void logout(String token) {
        if (token == null) {
            return;
        }
        Object value = this.logoutLockCache.getIfPresent(token);
        if (value == null) {
            this.logoutLockCache.put(token, DateHelper.getNow());
        }
    }

    public Pair<Boolean, String> isLogout(String token) {
        if (token == null) {
            return new Pair<>(true, null);
        }
        String value = this.logoutLockCache.getIfPresent(token);
        return new Pair<>(value != null, value);
    }


}
