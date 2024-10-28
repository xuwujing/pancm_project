package com.zans.base.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zans.mms.vo.user.MenuItem;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class LocalCacheConfig {

    private static final int DEFAULT_MAXSIZE = 1000;
    private static final int DEFAULT_TTL = 1;



    private Cache<Integer, List<MenuItem>> menuItemCache;


    @PostConstruct
    private void init() {
        menuItemCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();
    }

    public List<MenuItem> getMenuItemCache(Integer userId) {
        Object value = this.menuItemCache.getIfPresent(userId);
        if (value != null) {
            return (List<MenuItem>) value;
        }
        return null;
    }

    public void putMenuItemCache(Integer userId, List<MenuItem> menuItemList) {
        this.menuItemCache.put(userId, menuItemList);
    }



    /**
     * 创建基于Caffeine的Cache Manager
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = new ArrayList<>();
        for (Caches c : Caches.values()) {
            caches.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize())
                            .build())
            );
        }

        cacheManager.setCaches(caches);
        return cacheManager;
    }

    /**
     * 定义不同的cache名称、超时市场（秒）、最大容量。
     * <p>
     * 每个cache缺省：1秒超时、最多缓存1000条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches {
        /**
         *
         */
        JWT_TOKEN_USER(30, 100),
        JWT_TOKEN_VERITY(30, 100),
        CONSTANT_MAP(300, 100),
        CONSTANT_LIST(300, 100),
        AREA_LIST(300, 100),
        REGION_LIST(300, 100),
        REGION_LIST_ALL(300, 100),
        AREA_MAP(300, 100),
        DEVICE_TYPE_LIST(300, 100),
        DEVICE_TYPE_TEMPLATE_LIST(300, 100),
        DEVICE_TYPE_MAP(300, 100),
        PERM_ROUTE_MAP(300, 100),
        PERM_API_MAP(300, 100),
        PERM_API_MENU(600, 100),
        PERM_DATA_MENU(600, 10000),
        AREA_LIST_BY_PARENT(300, 100),
        AREA_TREE_LIST(300, 100),
        ALL_AREA_TREE_LIST(300, 100),
        AREA_LIST_ALL(300, 100),
        BRAND(300, 100);


        private int ttl;
        /**
         * 最大数量
         */
        private int maxSize = DEFAULT_MAXSIZE;

        Caches() {
        }

        Caches(int ttl) {
            this.ttl = ttl;
        }

        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        // 过期时间（秒）
        // private int ttl = DEFAULT_TTL;

        public int getMaxSize() {
            return maxSize;
        }

        public int getTtl() {
            return ttl;
        }
    }
}
