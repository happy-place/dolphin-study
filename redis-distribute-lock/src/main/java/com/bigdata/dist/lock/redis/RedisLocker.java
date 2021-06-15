package com.bigdata.dist.lock.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.TimeUnit;

/**
 * 1.创建互斥锁，并设置有效期 t1
 * 2.启动异步线程以t2为周期给锁续命,t2 < t1
 */
public class RedisLocker {

    private final Logger logger = Logger.getLogger(RedisLocker.class);


    private JedisPool jedisPool;

    private SetParams setParams;
    private String lockKey;
    private String id;
    private long millisecondsToExpire;

    public RedisLocker(String lockKey, String id, long millisecondsToExpire) {
        this.jedisPool =  new JedisPool("hadoop01",6379);
        this.setParams = SetParams.setParams().nx().px(millisecondsToExpire);
        this.lockKey = lockKey;
        this.id = id;
        this.millisecondsToExpire = millisecondsToExpire;
    }

    public void lock(){
        Jedis client = jedisPool.getResource();
        while(true){
            try {
                String pong = client.set(lockKey, id, setParams);
                if("OK".equals(pong)){
                    logger.info("acquire lock success");
                    resetExpire();
                    break;
                }else{
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                client.close();
            }
        }
    }

    public void resetExpire(){
        new Thread(()->{
            logger.info("lock watch dog start");
            while(true){
                Jedis client = null;
                try {
                    TimeUnit.MILLISECONDS.sleep(millisecondsToExpire-2000);
                    client = jedisPool.getResource();
                    client.expire(lockKey,millisecondsToExpire);
                    logger.info("reset lock expire");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(client!=null){
                        client.close();
                    }
                }
            }

        }).start();
    }

    public void unlock(){
        Jedis client = jedisPool.getResource();
        try {
            if(client.exists(lockKey) && id.equals(client.get(lockKey))){
                client.del(lockKey);
                logger.info("unlock success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
    }


}
