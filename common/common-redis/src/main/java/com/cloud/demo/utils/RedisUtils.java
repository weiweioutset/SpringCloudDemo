package com.cloud.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author weiwei
 * @Date 2022/5/12 下午11:07
 * @Version 1.0
 * @Desc
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;
    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean setValue(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public boolean setValue(final String key, Object value, Long expireTime , TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object getValue(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    public void hashSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }

    /**
     * 哈希 删除
     * @param key
     * @param hashKey
     */
    public void hashRemove(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.delete(key, hashKey);
    }

    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public Object hashGet(String key, Object hashKey) {
        HashOperations<String, Object, Object>  hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }

    /**
     * 获取key对应的所有map键值对
     * @param key
     * @return
     */
    public Map hashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 列表添加(添加到表的末尾)
     * @param k
     * @param v
     */
    public void lPush(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }
    /**
     * 列表获取
     * @param k
     * @param start
     * @param end
     * @return
     */
    public List<Object> lRange(String k, long start, long end){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,start,end);
    }
    /**
     * 集合添加
     * @param key
     * @param value
     */
    public void setAdd(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }

    /**
     * 集合获取
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param source
     */
    public void zAdd(String key,Object value,double source) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,source);
    }

    /**
     * 有序集合移除
     * @param key
     * @param value
     */
    public void zRemove(String key, Object value) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.remove(key, value);
    }

    /**
     * 有序集合移除
     * @param key
     * @param min
     * @param max
     */
    public void zRemoveByScore(String key, double min, double max) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.removeRangeByScore(key, min, max);
    }

    /**
     * 有序集合获取
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zRange(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.range(key, start, end);
    }

    /**
     * 有序集合获取
     * @param key
     * @param min
     * @param max
     * @param offset 偏移量
     * @param count 数量
     * @return
     */
    public Set<Object> rangeByScore(String key, double min, double max, long offset, long count){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, min, max, offset, count);
    }

    /**
     * 有序集合获取
     * @param key
     * @param offset 偏移量
     * @param count 数量
     * @return
     */
    public Set<Object> rangeByScore(String key, long offset, long count){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, Double.MIN_VALUE, Double.MAX_VALUE, offset, count);
    }

    /**
     * 获取有序集合的总数
     * @param key
     * @return
     */
    public Long countZSet(String key) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.count(key, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
     * 批量获取key
     * @param prefix
     * @return
     */
    public Set<String> scan(String prefix) {
        return scan(prefix,1000);
    }

    /**
     * 批量获取key
     * @param prefix
     * @param keysCount
     * @return
     */
    public Set<String> scan(String prefix, int keysCount) {
        if (prefix == null || "".equals(prefix)) {
            return null;
        }
        if (!prefix.contains(":")) {
            return Collections.emptySet();
        }
        Set<String> keys = (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(prefix + "*").count(keysCount).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });

        return keys;
    }
}
