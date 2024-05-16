package com.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import java.util.concurrent.TimeUnit;

public class DistributedRedisLock {
    //从配置类中获取redisson对象
    private static Redisson redisson = RedissonManager.getRedisson();
    private static final String LOCK_TITLE = "redisLock_";
    //加锁
    public static boolean acquire(String lockName) {
        //声明key对象
        String key = LOCK_TITLE + lockName;
        //获取锁对象
        RLock mylock = redisson.getLock(key);

        /**
         * 加锁，并且设置锁过期时间3秒，防止死锁的产生  uuid+threadId
         * lock()方法是阻塞获取锁的方式，如果当前锁被其他线程持有，则当前线程会一直阻塞等待获取锁，直到获取到锁或者发生超时或中断等情况才会结束等待。
         */
        mylock.lock(3l, TimeUnit.SECONDS);


        /**
         * 尝试获取锁
         * waitTimeout 尝试获取锁的最大等待时间，超过这个值，则认为获取锁失败
         * leaseTime   锁的持有时间,超过这个时间锁会自动失效（值应设置为大于业务处理的时间，确保在锁有效期内业务能处理完）
         *
         * tryLock()方法是一种非阻塞获取锁的方式，在尝试获取锁时不会阻塞当前线程，而是立即返回获取锁的结果，如果获取成功则返回true，否则返回false。
         * Redisson的tryLock()方法支持加锁时间限制、等待时间限制以及可重入等特性，可以更好地控制获取锁的过程和等待时间，避免程序出现长时间无法响应等问题
         **/
        boolean success = true;
        try {
            int waitTimeout = 10;
            int leaseTime = 30;
            success = mylock.tryLock((long)waitTimeout, (long)leaseTime, TimeUnit.SECONDS);
            if (success) {
                //成功获得锁，在这里处理业务
            }
        } catch (Exception ex) {
            throw new RuntimeException("aquire lock fail");
        }finally{//无论如何, 最后都要解锁
            // 判断当前线程是否持有锁
            if (success && mylock.isHeldByCurrentThread()) {
                mylock.unlock();
            }
        }


        //加锁成功
        return true;
    }
    //锁的释放
    public static void release(String lockName){
        //必须是和加锁时的同一个key
        String key = LOCK_TITLE + lockName;
        //获取所对象
        RLock mylock = redisson.getLock(key);
        //释放锁（解锁）
        mylock.unlock();
    }

}
