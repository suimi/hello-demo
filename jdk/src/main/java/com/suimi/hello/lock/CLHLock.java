package com.suimi.hello.lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author suimi
 * @date 2019/3/28
 */
public class CLHLock {
    class CLHNode {
        //false代表没人占用锁
        volatile boolean locked = true;
    }

    volatile CLHNode tail;// 指向最后一个申请锁的MCSNode
    //使用ThreadLocal保证每个线程副本内都有一个Node对象
    final ThreadLocal<CLHNode> current;

    private static final AtomicReferenceFieldUpdater<CLHLock, CLHNode> UPDATER =
        AtomicReferenceFieldUpdater.newUpdater(CLHLock.class, CLHNode.class, "tail");

    public CLHLock() {
        //初始化当前节点的node
        current = ThreadLocal.withInitial(() -> new CLHNode());

    }

    public void lock() throws InterruptedException {
        //得到当前线程的Node节点
        CLHNode own = current.get();
        //设置当前线程去注册锁，注意在多线程下环境下，这个
        //方法仍然能保持原子性，，并返回上一次的加锁节点（前驱节点）
        CLHNode preNode = UPDATER.getAndSet(this, own);
        if (preNode != null) {//已有线程占用了锁，进入自旋
            while (preNode.locked) {
                System.out.println(Thread.currentThread().getName() + " 开始自旋....  ");
                Thread.sleep(2000);
            }
        }
    }

    public void unlock() {
        CLHNode own = current.get();
        // 如果队列里只有当前线程，则释放对当前线程的引用（for GC）。
        boolean result = UPDATER.compareAndSet(this, own, null);
        System.out.println("update null: " + result);
        own.locked = false;// 改变状态，让后续线程结束自旋
    }
}
