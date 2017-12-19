package com.suimi.hello.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.RandomUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.*;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.fail;

/**
 * <ui>
 * <li>共享锁: 全局同步分布式锁, 同一时间两台机器只有一台能获得同一把锁.</li>
 * <li>共享读写锁: 用于分布式的读写互斥处理, 同时生成两个锁:一个读锁, 一个写锁,
 * 读锁能被多个应用持有, 而写锁只能一个独占, 当写锁未被持有时, 多个读锁持有者可以同时进行读操作</li>
 * <li>共享信号量: 在分布式系统中的各个JVM使用同一个zk lock path,
 * 该path将跟一个给定数量的租约(lease)相关联, 然后各个应用根据请求顺序获得对应的lease,
 * 相对来说, 这是最公平的锁服务使用方式.</li>
 * <li>多共享锁:内部构件多个共享锁(会跟一个znode path关联), 在acquire()过程中,
 * 执行所有共享锁的acquire()方法, 如果中间出现一个失败, 则将释放所有已require的共享锁;
 * 执行release()方法时, 则执行内部多个共享锁的release方法(如果出现失败将忽略)</li>
 * </ui>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ZookeeperLockTests {

    @Autowired
    private CuratorFramework client;

    public void init() {
        log.info("client status:{}", client.getState());
        if (client.getState() != CuratorFrameworkState.STARTED) {
            client.start();
        }
        log.info("client status:{}", client.getState());
    }

    @Test
    public void testSharedSemaphore() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(4);
        for (int i = 0; i < 1; i++) {
            testSharedSemaphore(i, latch);
        }
        latch.await(180, TimeUnit.SECONDS);
    }

    /**
     * 共享锁测试
     */
    @Test
    public void testSharedLock() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(2);
        Thread t1 = new Thread("t1") {
            public void run() {
                sharedLock();
                latch.countDown();
            }
        };
        Thread t2 = new Thread("t2") {
            public void run() {
                sharedLock();
                latch.countDown();
            }
        };
        t1.start();
        t2.start();
        latch.await(180, TimeUnit.SECONDS);
    }

    /**
     * @return void
     * @throws
     * @throws Exception
     * @描述：第一种锁: 共享锁
     * @createTime：2016年5月19日
     * @author: songqinghu
     */
    private void sharedLock() {
        //这两个都是共享锁
        // new InterProcessMutex(client, path)
        // new InterProcessSemaphoreMutex(client, path)

        InterProcessMutex sharedLock = new InterProcessMutex(client, "/sharedlock");
        String threadName = Thread.currentThread().getName();
        try {
            //锁是否被获取到
            //超时 不在进行操作
            if (sharedLock.acquire(50, TimeUnit.MILLISECONDS)) {
                //sharedLock.acquire();
                log.info(threadName + "  is  get the shared lock");
                Thread.sleep(1000);

            }

        } catch (Exception e) {
            //日志记录一下 超时说明 有锁 可以不在操作
            log.error(e.getMessage(), e);

        } finally {
            try {
                log.info(threadName + "  the flag is " + sharedLock.isAcquiredInThisProcess());
                if (sharedLock.isAcquiredInThisProcess()) {//判断是否持有锁 进而进行锁是否释放的操作
                    log.info(threadName + "  is  release the shared lock");
                    sharedLock.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @return void
     * @throws
     * @描述：测试读写锁
     * @createTime：2016年5月19日
     * @author: songqinghu
     */
    @Test
    public void testReadWriterLock() throws InterruptedException {
        //创建多线程 循环进行锁的操作
        final CountDownLatch latch = new CountDownLatch(15);
        InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(client, "/readwriter");
        final InterProcessMutex readLock = readWriteLock.readLock();
        final InterProcessMutex writeLock = readWriteLock.writeLock();

        List<Thread> jobs = new ArrayList<Thread>();

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread("写锁  " + i) {

                public void run() {
                    readWriterLock(writeLock);
                    latch.countDown();
                }
            };
            jobs.add(t);
        }

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread("读锁  " + i) {

                public void run() {
                    readWriterLock(readLock);
                    latch.countDown();
                }
            };
            jobs.add(t);
        }

        for (Thread thread : jobs) {
            thread.start();
        }
        latch.await(180, TimeUnit.SECONDS);
    }

    /**
     * @return void
     * @throws
     * @描述：读写锁演示
     */
    private void readWriterLock(InterProcessLock lock) {
        log.info(Thread.currentThread().getName() + "   进入任务 " + System.currentTimeMillis());
        try {
            if (lock.acquire(30000, TimeUnit.MILLISECONDS)) {
                //System.err.println(Thread.currentThread().getName() + " 等待超时  无法获取到锁");
                //执行任务 --读取 或者写入
                int time = RandomUtils.nextInt(0, 10000);
                log.info(Thread.currentThread().getName() + "   执行任务开始");
                Thread.sleep(time);
                log.info(Thread.currentThread().getName() + "   执行任务完毕");
            } else {
                System.err.println(Thread.currentThread().getName() + " 等待超时  无法获取到锁");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (lock.isAcquiredInThisProcess()) lock.release();
            } catch (Exception e2) {

            }
        }

    }


    private void testSharedSemaphore(final int x, CountDownLatch latch) {
        final InterProcessSemaphoreV2 semaphoreV2 =
                /// new InterProcessSemaphoreV2(client, "/sharedsemaphore", 50);
                new InterProcessSemaphoreV2(client, "/sharedsemaphore", new SharedCount(client, "/semaphore", 2));
        List<Thread> jobs = new ArrayList<Thread>();
        for (int i = 0; i < 4; i++) {
            Thread thread = new Thread(x + " 共享信息锁 " + i) {
                public void run() {
                    sharedSemaphore(semaphoreV2);
                    latch.countDown();
                }
            };
            jobs.add(thread);
        }
        for (Thread thread : jobs) {
            thread.start();
        }

    }


    /**
     * 共享信号量
     * 设置总的数量 -->分布式情况下的最大并行数量
     * 按照请求顺序进行 执行权的分配
     * 可以设置超时 不执行 也可以设置 直到获取执行权 执行
     */
    private void sharedSemaphore(InterProcessSemaphoreV2 semaphoreV2) {
        // new InterProcessSemaphoreV2(client, path, maxLeases)
        // new InterProcessSemaphoreV2(client, path, count)

        Lease lease = null;
        try {
            // lease = semaphoreV2.acquire(10, TimeUnit.MILLISECONDS);
            lease = semaphoreV2.acquire();
            if (lease != null) {
                log.info(Thread.currentThread().getName() + "   执行任务开始" + System.currentTimeMillis());
                //Thread.sleep(RandomUtils.nextInt(4000));
                Thread.sleep(1000);
                log.info(Thread.currentThread().getName() + "   执行任务完毕" + System.currentTimeMillis());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (lease != null) semaphoreV2.returnLease(lease);
            } catch (Exception e2) {
                e2.printStackTrace();
            }


        }


    }
}
