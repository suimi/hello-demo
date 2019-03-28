package com.suimi.hello.lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author suimi
 * @date 2019/3/28
 */
public class MCSLock {
    public static class MCSNode {
        volatile MCSNode next;
        volatile boolean isBlock = true; // 默认是在等待锁
    }

    volatile MCSNode tail;// 指向最后一个申请锁的MCSNode
    private static final AtomicReferenceFieldUpdater<MCSLock, MCSNode> UPDATER =
        AtomicReferenceFieldUpdater.newUpdater(MCSLock.class, MCSNode.class, "tail");

    ThreadLocal<MCSNode> current;

    public MCSLock() {
        //初始化当前节点的node
        current = ThreadLocal.withInitial(() -> new MCSNode());
    }

    public void lock() throws InterruptedException {
        //获取当前线程的Node
        MCSNode currentNode = current.get();
        //获取前驱节点
        MCSNode predecessor = UPDATER.getAndSet(this, currentNode);// step 1
        //如果前驱节点不为null，说明有线程已经占用
        if (predecessor != null) {
            predecessor.next = currentNode;// step 2

            while (currentNode.isBlock) {// step 3
                System.out.println(Thread.currentThread().getName() + " 开始自旋....  ");
                Thread.sleep(2000);
            }
        } else { // 只有一个线程在使用锁，没有前驱来通知它，所以得自己标记自己为非阻塞
            currentNode.isBlock = false;
        }
    }

    public void unlock() {
        //获取自己的节点
        MCSNode currentNode = current.get();
        if (currentNode.isBlock) {// 锁拥有者进行释放锁才有意义
            return;
        }

        if (currentNode.next == null) {// 检查是否有人排在自己后面
            if (UPDATER.compareAndSet(this, currentNode, null)) {// step 4
                // compareAndSet返回true表示确实没有人排在自己后面
                return;
            } else {
                // 突然有人排在自己后面了，可能还不知道是谁，下面是等待后续者
                // 这里之所以要忙等是因为：step 1执行完后，step 2可能还没执行完
                while (currentNode.next == null) { // step 5
                    System.out.println(Thread.currentThread().getName() + " 等待....  ");
                }
            }
        }
        currentNode.next.isBlock = false;
        currentNode.next = null;// for GC
    }
}
