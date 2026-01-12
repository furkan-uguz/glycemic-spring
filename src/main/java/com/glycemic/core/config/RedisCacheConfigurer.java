package com.glycemic.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glycemic.core.util.CacheNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfigurer {

    @Value("${spring.redis.username}")
    private String redisUsername;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        serverConfig.setUsername(redisUsername);
        serverConfig.setPassword(redisPassword);

        return new LettuceConnectionFactory(serverConfig);
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTypeFactory(objectMapper.getTypeFactory().withClassLoader(loader));

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        return RedisCacheConfiguration
                .defaultCacheConfig(Thread.currentThread().getContextClassLoader())
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> {
            builder.withCacheConfiguration(CacheNames.CITY_CACHE, RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader()).entryTtl(Duration.ofHours(24)));
            builder.withCacheConfiguration(CacheNames.CATEGORY_CACHE, RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader()).entryTtl(Duration.ofMinutes(30)));
        };
    }
}
