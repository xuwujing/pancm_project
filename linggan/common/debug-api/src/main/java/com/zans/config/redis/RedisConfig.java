package com.zans.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;


/**
 * @author beixing
 * @Title: RedisAuthConfig
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2022/3/22
 **/
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 配置lettuce连接池(多数据源的公共参数)
     * GenericObjectPoolConfig不是线程安全的
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    public GenericObjectPoolConfig redisPool() {
        return new GenericObjectPoolConfig<>();
    }

    /**
     * 配置第一个数据源的
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.auth")
    public RedisStandaloneConfiguration redisAuthConfig() {
        return new RedisStandaloneConfiguration();
    }

    /**
     * 配置第二个数据源
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.acct")
    public RedisStandaloneConfiguration redisAcctConfig() {
        return new RedisStandaloneConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.alive")
    public RedisStandaloneConfiguration redisAliveConfig() {
        return new RedisStandaloneConfiguration();
    }

    @Value("${spring.redis.auth.database:2}")
    private int authDataBase;
    @Value("${spring.redis.auth.host:1}")
    private String authHost;
    @Value("${spring.redis.auth.port:6379}")
    private int authPort;
    @Value("${spring.redis.auth.password}")
    private String authPwd;


    @Value("${spring.redis.acct.database:3}")
    private int acctDataBase;
    @Value("${spring.redis.acct.host:1}")
    private String acctHost;
    @Value("${spring.redis.acct.port:6379}")
    private int acctPort;
    @Value("${spring.redis.acct.password}")
    private String acctPwd;

    @Value("${spring.redis.alive.database:10}")
    private int aliveDataBase;
    @Value("${spring.redis.alive.host:1}")
    private String aliveHost;
    @Value("${spring.redis.alive.port:6379}")
    private int alivePort;
    @Value("${spring.redis.alive.password}")
    private String alivePwd;

    /**
     * 配置第一个数据源的连接工厂
     * 这里注意：需要添加@Primary 指定bean的名称，目的是为了创建两个不同名称的LettuceConnectionFactory
     *
     * @param config
     * @param redisAuthConfig
     * @return
     */
    @Bean("authFactory")
    @Primary
    public LettuceConnectionFactory authFactory(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisAuthConfig) {
        redisAuthConfig.setDatabase(authDataBase);
        redisAuthConfig.setHostName(authHost);
        redisAuthConfig.setPort(authPort);
        redisAuthConfig.setPassword(authPwd);
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisAuthConfig, clientConfiguration);
    }


    @Bean("acctFactory")
    public LettuceConnectionFactory acctFactory(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisAcctConfig) {
        redisAcctConfig.setDatabase(acctDataBase);
        redisAcctConfig.setHostName(acctHost);
        redisAcctConfig.setPort(acctPort);
        redisAcctConfig.setPassword(acctPwd);
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisAcctConfig, clientConfiguration);
    }

    @Bean("aliveFactory")
    public LettuceConnectionFactory aliveFactory(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisAliveConfig) {
        redisAliveConfig.setDatabase(aliveDataBase);
        redisAliveConfig.setHostName(aliveHost);
        redisAliveConfig.setPort(alivePort);
        redisAliveConfig.setPassword(alivePwd);
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisAliveConfig, clientConfiguration);
    }


    /**
     * 配置第一个数据源的RedisTemplate
     * 注意：这里指定使用名称=factory 的 RedisConnectionFactory
     * 并且标识第一个数据源是默认数据源 @Primary
     *
     * @param factory
     * @return
     */
    @Bean("redisAuthTemplate")
    @Primary
    public RedisTemplate<Serializable, Serializable> redisAuthTemplate(@Qualifier("authFactory") RedisConnectionFactory factory) {
        RedisTemplate<Serializable, Serializable> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //开启事务
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置第一个数据源的RedisTemplate
     *
     *
     * @param acctFactory
     * @return
     */
    @Bean("redisAcctTemplate")
    public RedisTemplate<String, Object> redisAcctTemplate(@Qualifier("acctFactory") RedisConnectionFactory acctFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(acctFactory);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //开启事务
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    @Bean("redisAliveTemplate")
    public RedisTemplate<String, Object> redisAliveTemplate(@Qualifier("aliveFactory") RedisConnectionFactory aliveFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(aliveFactory);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //开启事务
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 注入封装RedisTemplate
     */
    @Bean(name = "redisUtil")
    public RedisUtil redisUtil(@Qualifier("redisAcctTemplate") RedisTemplate<String, Object> redisAcctTemplate) {
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisAcctTemplate);
        return redisUtil;
    }

}
