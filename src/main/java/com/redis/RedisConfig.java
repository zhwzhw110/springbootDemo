package com.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/20 0020 下午 2:18
 * @DESC : Redis的基本配置
 */
@Configuration
public class RedisConfig {


    @Bean
    public JedisPool jedisPool(@Qualifier(value = "jedisPoolConfig") JedisPoolConfig jedisPoolConfig){
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,"127.0.0.1",6379);
        return jedisPool;
    }

    @Bean(value = "jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

}
