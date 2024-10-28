package com.zans.base.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zans.base.config.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
* @Title: LockCacheHelper
* @Description:
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/21/21
*/
@Slf4j
@Component
public class ReqCountLockCacheHelper {

    /**
     * 请求次数缓存
     */
    Cache<String, Integer> reqAttemptCache;

    /**
     * 请求，锁定缓存
     */
    Cache<String, Object> reqLockCache;


    @PostConstruct
    private void init() {
        log.info("Req Cache init");
        reqAttemptCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.DAYS)
                .maximumSize(10_000)
                .build();

        reqLockCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();

    }


    /**
    * @Author beiming
    * @Description  请求次数递增
    * @Date  4/21/21
    * @Param
    * @return
    **/
    public void increaseReqAttempt(String key) {
        if (key == null) {
            return;
        }
        Integer value = reqAttemptCache.getIfPresent(key);
        if (value == null) {
            value = 0;
        } else {
            value += 1;
        }
        if (value > GlobalConstants.REQ_MAX_ATTEMPT) {
            if (reqLockCache.getIfPresent(key) == null) {
                reqLockCache.put(key, value);
            }
        }
        log.info("reqAttemptCache#{}, {}, reqLockCache#{}", key, value, reqLockCache.getIfPresent(key));
        reqAttemptCache.put(key, value);
    }

    /**
     * @Author beiming
     * @Description  请求次数递减
     * @Date  4/21/21
     * @Param
     * @return
     **/
    public void decreaseReqAttempt(String key) {
        if (key == null) {
            return;
        }
        Integer value = reqAttemptCache.getIfPresent(key);
        if (value == null) {
            value = 0;
        } else {
            value -= 1;
        }
        if (value <=GlobalConstants.REQ_MAX_ATTEMPT) {
            if (reqLockCache.getIfPresent(key) != null) {
                reqLockCache.invalidate(key);
            }
        }
        log.info("reqAttemptCache#{}, {}, reqLockCache#{}", key, value, reqLockCache.getIfPresent(key));
        reqAttemptCache.put(key, value);
    }



    /**
    * @Author beiming
    * @Description   清空请求次数,锁缓存
    * @Date  4/21/21
    * @Param
    * @return
    **/
    public void removeReqAttempt(String key) {
        if (key == null) {
            return;
        }
        reqAttemptCache.invalidate(key);
        reqLockCache.invalidate(key);
    }

    /**
    * @Author beiming
    * @Description  判断是否锁住
    * @Date  4/21/21
    * @Param
    * @return
    **/
    public boolean isReqLocked(String key) {
        if (key == null) {
            return false;
        }
        return this.reqLockCache.getIfPresent(key) != null;
    }


}
