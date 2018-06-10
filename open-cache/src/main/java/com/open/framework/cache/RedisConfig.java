package com.open.framework.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.utils.YamlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.*;

/**
 * redis配置初始化类
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(name = BaseConstant.CACHE_TYPE, havingValue = BaseConstant.CACHE_REDIS)
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = null;
        String host = YamlUtils.DefaultGetYamlValue("spring.redis.host");
        if (StringUtils.isNotEmpty(host)) {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(host);
            String port = YamlUtils.DefaultGetYamlValue("spring.redis.port");
            redisStandaloneConfiguration.setPort(StringUtils.isNotEmpty(port) ? Integer.parseInt(port) : 6379);
            redisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        } else {
            Map<String, Object> source = new HashMap<String, Object>();
            source.put("spring.redis.cluster.nodes", YamlUtils.DefaultGetYamlValue("spring.redis.cluster.nodes"));
            RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration((PropertySource) (new
                    MapPropertySource("RedisClusterConfiguration", source)));
            redisConnectionFactory = new JedisConnectionFactory(clusterConfig);
        }
        return redisConnectionFactory;
    }

    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory());
        Set<String> cacheNames = new HashSet<String>() {{
            add(BaseConstant.CACHE_FRAMEWORK);
        }};
        builder.initialCacheNames(cacheNames);
        return builder.build();
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}