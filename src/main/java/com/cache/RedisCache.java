package com.cache;

import com.redis.JedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/20 0020 下午 4:31
 * @DESC :
 */
@Component
public class RedisCache<K,V> implements Cache<K,V>{

    @Resource(name = "jedisUtils")
    private JedisUtils jedisUtils;

    private final static String REDIS_CACHE_MANAGE_PREFIX= "CacheManage:";

    private byte[] getKey(K k){
        if(k instanceof  String){ //如果是字符串，直接拼接
            return (REDIS_CACHE_MANAGE_PREFIX+k).getBytes();
        }
        //如果不是字符串，则先把key序列化以后再凭借
        return (REDIS_CACHE_MANAGE_PREFIX+SerializationUtils.serialize(k).toString()).getBytes();
    }

    //获取value
    @Override
    public V get(K k) throws CacheException {
        //这个地方可以使用本地二级缓存，没有必要每次都去redis中取？？什么意思
        byte[] key = getKey(k);
        byte[] value = jedisUtils.get(key);
        if(value!=null){
            return (V)SerializationUtils.deserialize(value); //反序列化成对象
        }
        return null;
    }

    //保存数据
    @Override
    public V put(K k, V v) throws CacheException {
       byte[] key = getKey(k);
       byte[] value = SerializationUtils.serialize(v);
       jedisUtils.set(key,value);
       jedisUtils.expire(key,600); //设置超时时间
        if(value != null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    //删除数据
    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtils.get(key);
        jedisUtils.del(key);
        if(value != null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }
    //清空缓存
    @Override
    public void clear() throws CacheException {

    }

    //返回dbSize
    @Override
    public int size() {
        return jedisUtils.size();
    }


    //获取所有的keys
    @Override
    public Set<K> keys() {
        Set<K> keys = new HashSet<K>();
        Set<byte[]> redisKeys = jedisUtils.keys(REDIS_CACHE_MANAGE_PREFIX);
        if(CollectionUtils.isEmpty(redisKeys)){
            return keys;
        }
        for(byte[] key:redisKeys){
            keys.add((K) SerializationUtils.deserialize(key));
        }
        return keys;
    }

    //查询CacheManage开头的所有的value
    @Override
    public Collection<V> values() {
        Set<V> values = new HashSet<V>();
        Set<byte[]> redisKeys = jedisUtils.keys(REDIS_CACHE_MANAGE_PREFIX);
        if(CollectionUtils.isEmpty(redisKeys)){
            return values;
        }
        for(byte[] key:redisKeys){
            byte[] value = jedisUtils.get(key);
            values.add((V) SerializationUtils.deserialize(value));
        }
        return values;
    }
}
