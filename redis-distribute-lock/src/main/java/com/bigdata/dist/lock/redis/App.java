package com.bigdata.dist.lock.redis;


import java.util.UUID;

public class App {
    /**
     * 基于redis 分布式锁
     * 1.创建互斥锁，并设置有效期 t1
     * 2.启动异步线程以t2为周期(t2<t1)，给锁续命;
     * @param args
     */
    public static void main(String[] args) {
        DistrubuteWorker distrubuteWorker = new DistrubuteWorker(UUID.randomUUID().toString());
        distrubuteWorker.start();
    }

}


