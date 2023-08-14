package com.brenden.cloud.redis.utils;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/8/13
 */
@RequiredArgsConstructor
public class RedissonUtils {

    /** 1小时过期 */
    public static final long HOUR_ONE = 60 * 60;

    /** 1天过期 */
    public static final long DAY_ONE = 60 * 60 * 24;

    /** 2小时过期 */
    public static final long HOUR_TWO = 60 * 60 * 2;

    /** 永不过期 */
    public static final long NOT_EXPIRE = -1;




    private final RedissonClient redissonClient;

    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    public RLock getFairLock(String key) {
        return redissonClient.getFairLock(key);
    }

    public RLock getReadLock(String key) {
        return redissonClient.getReadWriteLock(key).readLock();
    }

    public RLock getWriteLock(String key) {
        return redissonClient.getReadWriteLock(key).writeLock();
    }

    public boolean tryLock(RLock lock, long expire, long timeout) throws InterruptedException {
        return lock.tryLock(timeout, expire, TimeUnit.MILLISECONDS);
    }

    public boolean tryLock(String key, long expire, long timeout) throws InterruptedException {
        return tryLock(getLock(key), expire, timeout);
    }

    public void unlock(String key) {
        unlock(getLock(key));
    }

    public void unlock(RLock lock) {
        lock.unlock();
    }

    public void lock(String key) {
        lock(getLock(key));
    }

    public void lock(RLock lock) {
        lock.lock();
    }

    public void hSet(String key, String field, Object value, long expire) {
        RMap<Object, Object> map = redissonClient.getMap(key);
        map.put(field, value);
        map.expire(Duration.ofSeconds(expire));
    }

    public void hSet(String key, String field, Object value) {
        hSet(key, field, value, NOT_EXPIRE);
    }

    public Object hGet(String key, String field) {
        return redissonClient.getMap(key).get(field);
    }


    public Object get(String key) {
        return redissonClient.getBucket(key).get();
    }

    public boolean delete(String key) {
        return redissonClient.getKeys().delete(key) > 0;
    }

    public boolean hasHashKey(String key, String field) {
        return redissonClient.getMap(key).containsKey(field);
    }


}
