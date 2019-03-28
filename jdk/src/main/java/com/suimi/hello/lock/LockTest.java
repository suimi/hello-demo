package com.suimi.hello.lock;

/**
 * @author suimi
 * @date 2019/3/28
 */
public class LockTest {
    public static void main(String[] args) {

//        MCSLock lock = new MCSLock();
        CLHLock lock = new CLHLock();
        Runnable runnable = () -> {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "  获得锁 ");
                //前驱释放，do own work
                Thread.sleep(4000);
                System.out.println(Thread.currentThread().getName() + "  释放锁 ");
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread t1 = new Thread(runnable, "线程1");
        Thread t2 = new Thread(runnable, "线程2");
        Thread t3 = new Thread(runnable, "线程3");

        t1.start();
        t2.start();
        t3.start();
    }
}
