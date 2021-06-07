package com.bigdata.dist.lock.zk;

public class App {
    /**
     * 基于 zk 分布式锁
     * 所有工作节点启动时，先检查临时路径是否存在，如果存在说明有master正在工作，其余节点作为热备节点，轮询检查临时路径存续状态，
     * 直至master节点失联，临时路径注销。
     * 然后热备节点竞争锁，成功获取锁的机器，再次检查临时路径是否存在（双端检锁），
     *  如果依旧不存在，则创建新的临时路径，然后释放锁
     *  如果存在，则说明被其他节点抢占，继续轮询检查
     * @param args
     */
    public static void main(String[] args) {
        DistrubuteWorker distrubuteWorker = new DistrubuteWorker();
        distrubuteWorker.start();
    }

}


