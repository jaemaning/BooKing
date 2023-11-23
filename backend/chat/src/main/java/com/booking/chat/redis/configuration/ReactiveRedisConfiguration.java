package com.booking.chat.redis.configuration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ReactiveRedisConfiguration{

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;


    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        standaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(standaloneConfiguration);
    }

    @Bean
    @Qualifier
    public ReactiveRedisTemplate<String, Set<Long>> reactiveRedisSetTemplate() {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(Set.class, Long.class);
        Jackson2JsonRedisSerializer<Set<Long>> setJsonSerializer = new Jackson2JsonRedisSerializer<>(type);

        RedisSerializationContext<String, Set<Long>> serializationContext = RedisSerializationContext
            .<String, Set<Long>>newSerializationContext(stringSerializer)
            .key(stringSerializer)
            .value(setJsonSerializer)
            .hashKey(stringSerializer)
            .hashValue(setJsonSerializer)
            .build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory(), serializationContext);
    }
}
