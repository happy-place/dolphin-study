package com.bigdata.dist.lock.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ZKMasterClient {

    private final Logger logger = Logger.getLogger(ZKMasterClient.class);

    private InterProcessMutex lock;

    private CuratorFramework client;

    //操作失败重试机制 1000毫秒间隔 重试3次
    private RetryPolicy retryPolicy;

    private String lockPath;
    private String ephemeralPath;

    public ZKMasterClient(String zks,int baseSleepTimeMs, int maxRetries,String lockPath,String ephemeralPath) {
        this.lockPath = lockPath;
        this.ephemeralPath = ephemeralPath;
        this.retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        this.client = CuratorFrameworkFactory.newClient(zks, this.retryPolicy);
        this.client.start();
    }

    public void tryLock() throws Exception{
        while(true){
            if(!isExisted(ephemeralPath)){ // 双重检锁
                acquireLock(this.lockPath);
                if(!isExisted(ephemeralPath)){
                    persistEphemeral(this.ephemeralPath,"placeholder");
                    break;
                }
            }else{
                TimeUnit.SECONDS.sleep(5);
            }
        }


    }

    public void persistEphemeral(final String path, final String value) {
        try {
            // If the ephemeral node exist and the data is not equals to the given value
            // delete the old node
            if (isExisted(path) && !value.equals(get(path))) {
                try {
                    client.delete().deletingChildrenIfNeeded().forPath(path);
                } catch (KeeperException.NoNodeException ignore) {
                    //NOP
                }
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, value.getBytes(StandardCharsets.UTF_8));
        } catch (final Exception ex) {
            logger.error("persistEphemeral path : " +path+ ", value : "+ value);
        }
    }

    public boolean isExisted(final String key) {
        try {
            return client.checkExists().forPath(key) != null;
        } catch (Exception ex) {
            logger.error("isExisted key : {}"+ key);
        }
        return false;
    }

    public String get(final String key) {
        try {
            return new String(client.getData().forPath(key), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            logger.error("get key : "+key);
        }
        return null;
    }

    public void acquireLock(String path) throws Exception {
        lock = new InterProcessMutex(client, path); // 临时节点，宕机自动释放

        logger.debug("wait for zk mutex lock");
        //阻塞方法，获取不到锁线程会挂起。
        lock.acquire();
        logger.debug("acquire lock success");
    }


    public void waitForEphemeralRelease(String ephemeralPath) throws InterruptedException {
        while(isExisted(ephemeralPath)){
            TimeUnit.SECONDS.sleep(5);
        }
    }

    public void releaseLock() throws Exception {
        lock.release();
        logger.debug("release lock success");
    }

    public void close() throws Exception {
        client.close();
        logger.debug("close zk client success");
    }


}
