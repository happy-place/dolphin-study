package com.bigdata.dist.lock.redis;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DistrubuteWorker {

    private final Logger logger = Logger.getLogger(DistrubuteWorker.class);

    private RedisLocker lock;

    public DistrubuteWorker(String id) {
        this.lock = new RedisLocker("mylock",id,5000);
    }

    public void start(){
        try {
            lock.lock();
            runJob();
            addHook();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void runJob(){
        new Thread(() -> {
            while(true){
                String str = new Date().toString();
                logger.info(str);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void addHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            try {
                lock.unlock();
                logger.info("shutdown zk client");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }



}