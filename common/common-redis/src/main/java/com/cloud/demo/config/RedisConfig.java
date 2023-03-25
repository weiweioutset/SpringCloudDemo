package com.cloud.demo.config;

import com.cloud.demo.RedisCacheSpaceExpireConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午4:13
 * @Version 1.0
 * @Desc
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    /** 默认的java8日期时间格式 */
    private static final String    DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 默认的java8日期格式 */
    private static final String    DEFAULT_DATE_FORMAT      = "yyyy-MM-dd";
    /** 默认的java8时间格式 */
    private static final String    DEFAULT_TIME_FORMAT      = "HH:mm:ss";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisTemplate redisTemplate) {
        // 配置默认过期时间和序列化机制
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60L))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));

        // 自定义缓存空间应用不同配置，可以配置不同的过期时间
        Set<String> cacheNames =  new HashSet<>();
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>(RedisCacheSpaceExpireConfig.EXPIRE_TIME_MAP.size());
        for (Map.Entry<String, Integer> entry : RedisCacheSpaceExpireConfig.EXPIRE_TIME_MAP.entrySet()) {
            String cacheName = entry.getKey();
            Integer expireMinute = entry.getValue();
            cacheNames.add(cacheName);
            configMap.put(cacheName, defaultCacheConfig.entryTtl(Duration.ofMinutes(expireMinute)));
        }

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key都采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        // value的序列化方式采用jackson
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        // 解决查询缓存转换异常的问题
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        // 添加java8时间相关序列化/反序列化处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
