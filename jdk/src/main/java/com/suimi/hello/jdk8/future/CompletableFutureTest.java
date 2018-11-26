/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.jdk8.future;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author suimi
 * @date 2018/5/24
 */
public class CompletableFutureTest extends TestA {

    @Test public void test() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int i = 1 / 0;
            return 100;
        });
        System.out.println(future.join());
        //        System.out.println(future.get());

    }

    @Test public void test1() throws ExecutionException, InterruptedException {
        CompletableFutureTest test = new CompletableFutureTest();
        System.out.println(test.print().get());
    }

    @Test public void testCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.completedFuture(100);
        System.out.println(future.get());
        CompletableFuture<Integer> f1 = future.thenCompose(this::dev);
        System.out.println(f1.get());
        System.out.println(future.get());
    }

    public CompletableFuture<Integer> dev(Integer integer) {
        return CompletableFuture.supplyAsync(() -> integer / 10);
    }

    @Override public CompletableFuture<TestA> print() {
        return super.print().thenRun(this::print2).thenApply(t -> this);
    }

    public void print2() {
        System.out.println("print");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("print1W");
    }

    public String hello(Void v) {
        System.out.print("hello " + v);
        return "hello";
    }

    private volatile CompletableFuture<Void> completableFuture;

    @Test public void test3() {
        completableFuture = new CompletableFuture<>();
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    //模拟执行耗时任务
                    System.out.println("task doing...");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException("抛异常了");
                } catch (Exception e) {
                    //告诉completableFuture任务发生异常了
                    completableFuture.completeExceptionally(e);
                }
            }
        }).start();
        System.out.println("start future");
        CompletableFuture<Void> future = completableFuture.whenComplete((result, error) -> completableFuture = null);
        future.whenComplete((result, error) -> {
            System.out.println("when complete");
            if (error == null) {
                future.complete(null);
            } else {
                future.completeExceptionally(error);
            }
        });
        future.join();
        System.out.println("end");
    }

    @Test public void test4() {

        CompletableFuture joinFuture = new CompletableFuture<>();

        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f1";

        });

        f1.whenCompleteAsync((result, error) -> {
            if (error != null) {
                joinFuture.completeExceptionally(error);
            } else {
                System.out.println("error");
                int i = 1/0;
                joinFuture.complete(result);
            }
        });


        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "f2";
        });

        f2.whenCompleteAsync(new BiConsumer<String, Throwable>() {
            @Override public void accept(String s, Throwable throwable) {
                System.out.println(System.currentTimeMillis() + ":" + s);
            }
        });

        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "f3";
        });

        f3.whenCompleteAsync(new BiConsumer<String, Throwable>() {
            @Override public void accept(String s, Throwable throwable) {
                System.out.println(System.currentTimeMillis() + ":" + s);
            }
        });

        CompletableFuture<String> f4 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "f4";
        });

        f4.whenCompleteAsync(new BiConsumer<String, Throwable>() {
            @Override public void accept(String s, Throwable throwable) {
                System.out.println(System.currentTimeMillis() + ":" + s);
            }
        });

        CompletableFuture<Void> f12 = CompletableFuture.allOf(joinFuture, f2);

        CompletableFuture<Void> all = CompletableFuture.allOf(f12, f3, f4);

        //阻塞，直到所有任务结束。
        System.out.println(System.currentTimeMillis() + ":阻塞");
        all.join();

        System.out.println(System.currentTimeMillis() + ":阻塞结束");

        //一个需要耗时2秒，一个需要耗时3秒，只有当最长的耗时3秒的完成后，才会结束。
        System.out.println("任务均已完成。");
    }

    private volatile CompletableFuture<Void> joinFuture;
    @Test
    public void test5() {
        joinFuture = new CompletableFuture<>();
        CompletableFuture<String> join = join();
        CompletableFuture<Void> future = joinFuture.whenComplete((result, error) -> joinFuture = null);
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "f3";
        });

        f3.whenCompleteAsync(new BiConsumer<String, Throwable>() {
            @Override public void accept(String s, Throwable throwable) {
                System.out.println(System.currentTimeMillis() + ":" + s);
            }
        });

        CompletableFuture<Void> all = CompletableFuture.allOf(join,joinFuture, f3);
        all.join();
    }

    private CompletableFuture<String> join() {
        CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f";

        });

        CompletableFuture<String> ef = f.whenCompleteAsync((result, error) -> {
            if (error != null) {
                joinFuture.completeExceptionally(error);
            } else {
                System.out.println("error");
                int i = 1 / 0;
                joinFuture.complete(null);
            }
        });
        ef.whenComplete((r,e)->{
            if (e == null) {
                ef.complete(r);
            } else {
                ef.completeExceptionally(e);
            }
        });
        return ef;
    }
}
