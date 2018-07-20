package com.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import javax.annotation.Resource;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/20 0020 下午 4:17
 * @DESC : 创建自定义的CustomCache
 */
public class CustomCacheManager implements CacheManager{

    @Resource
    private RedisCache redisCache;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return redisCache;
    }
}
