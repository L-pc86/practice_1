package org.example.test1.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        
        // 设置 Key 的序列化器为 String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置 Value 的序列化器为 JSON
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        // 设置 Hash Key 的序列化器为 String
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // 设置 Hash Value 的序列化器为 JSON
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
