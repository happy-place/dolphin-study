package com.bigdata.dist.lock.zk;

import org.apache.log4j.Logger;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DistrubuteWorker {

    private final Logger logger = Logger.getLogger(DistrubuteWorker.class);

    private ZKMasterClient client;

    private String lockPath = "/mylock";
    private String ephemeralPath = lockPath + "/master";

    public DistrubuteWorker() {
        this.client = new ZKMasterClient(
                "hadoop01:2181,hadoop02:2181,hadoop03:2181",
                1000,
                3,
                lockPath,
                ephemeralPath
        );
    }

    public void start(){
        try {
            client.tryLock();
            runJob();
            addHook();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                client.releaseLock();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                client.close();
                logger.info("shutdown zk client");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }



}