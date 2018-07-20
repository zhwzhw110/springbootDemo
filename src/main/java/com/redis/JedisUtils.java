package com.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/20 0020 下午 2:12
 * @DESC : Redis访问的工具类
 */
@Component(value = "jedisUtils")
public class JedisUtils {
    @Autowired
    private JedisPool jedisPool;

    //获取连接的方法
    private Jedis getResources(){
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

    //jedis保存数据的方法
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResources();
        try {
            jedis.set(key,value); //保存到缓存中
            return  value;
        }finally {
            jedis.close();
        }
    }

    //jedis释放方法
    public void expire(byte[] key, int time) {
        Jedis jedis = getResources();
        try {
            jedis.expire(key,time); //设置指定key的指定超时时间 单位为秒
        }finally {
            jedis.close();
        }
    }
    //获取value的方法
    public byte[] get(byte[] key) {
        Jedis jedis = getResources();
        try {
            byte[] value = jedis.get(key);
            return value;
        }finally {
            jedis.close();
        }

    }

    //Jedis删除的方法
    public void del(byte[] key) {
        Jedis jedis = getResources();
        try {
            jedis.del(key);
        }finally {
            jedis.close();
        }
    }

    //根据前缀获取所有的keys
    public Set<byte[]> keys(String shiro_session_prifix) {
        Jedis jedis = getResources();
        try {
            Set<byte[]> keys = jedis.keys((shiro_session_prifix+"*").getBytes());
            return keys;
        }finally {
            jedis.close();
        }
    }
}
