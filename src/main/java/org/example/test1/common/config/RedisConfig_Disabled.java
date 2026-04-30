package org.example.test1.common.config;

/**
 * Redis配置类
 *
 * 使用说明：
 * 1. 安装Redis并启动服务
 * 2. pom.xml中取消Redis依赖的注释
 * 3. application.yml中取消Redis连接配置的注释
 * 4. 将本文件重命名为 RedisConfig.java（去掉 _Disabled 后缀）
 * 5. ShopController中切换为Redis版本
 *
 * 当前状态：未启用（文件名带 _Disabled 后缀，Spring不会扫描加载）
 */
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
*/
public class RedisConfig_Disabled {
}
