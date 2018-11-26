/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.jdk8.future;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author suimi
 * @date 2018/11/26
 */
public class CompletableFutureDemo {
    static Random random = new Random();

    @Test public void test1() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("msg");
        assertTrue(cf.isDone());
        assertEquals("msg", cf.getNow(null));
    }

    @Test public void test2() {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            //没有指定executor的情况下,异步执行通过ForkJoinPool实现， 它使用守护线程去执行任务
            System.out.println(
                "currentThread:" + Thread.currentThread().getName() + "is daemon:" + Thread.currentThread().isDaemon());
            assertTrue(Thread.currentThread().isDaemon());
            randomSleep();
        });
        assertFalse(cf.isDone());
        sleepEnough();
        assertTrue(cf.isDone());
    }

    @Test public void test3() {
        System.out.println(
            "currentThread:" + Thread.currentThread().getName() + "is daemon:" + Thread.currentThread().isDaemon());
        CompletableFuture<String> cf = CompletableFuture.completedFuture("msg").thenApply(s -> {
            assertFalse(Thread.currentThread().isDaemon());
            System.out.println(
                "currentThread:" + Thread.currentThread().getName() + "is daemon:" + Thread.currentThread().isDaemon());
            return s.toUpperCase();
        });
        assertEquals("MSG", cf.getNow(null));
    }

    @Test public void test4() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("msg").thenApplyAsync(s -> {
            assertTrue(Thread.currentThread().isDaemon());
            System.out.println(
                "currentThread:" + Thread.currentThread().getName() + "is daemon:" + Thread.currentThread().isDaemon());
            randomSleep();
            return s.toUpperCase();
        });
        assertNull(cf.getNow(null));
        assertEquals("MSG", cf.join());
    }

    @Test public void test41() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("msg").thenApplyAsync(s -> {
            assertTrue(Thread.currentThread().isDaemon());
            System.out.println(
                "currentThread:" + Thread.currentThread().getName() + "is daemon:" + Thread.currentThread().isDaemon());
            randomSleep();
            return s.toUpperCase();
        });
        assertNull(cf.getNow(null));
        sleepEnough();
        assertEquals("MSG", cf.getNow(null));
    }

    @Test public void test42() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("msg").thenApplyAsync(s -> {
            assertTrue(Thread.currentThread().isDaemon());
            System.out.println(
                "currentThread:" + Thread.currentThread().getName() + "is daemon:" + Thread.currentThread().isDaemon());
            randomSleep();
            return s.toUpperCase();
        });
        assertNull(cf.getNow(null));
        sleepEnough();
        assertEquals("MSG", cf.get());
    }

    static ExecutorService executor = Executors.newFixedThreadPool(3, new ThreadFactory() {
        int count = 1;

        @Override public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "custom-executor-" + count++);
        }
    });

    @Test public void test5() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("msg").thenApplyAsync(s -> {
            assertTrue(Thread.currentThread().getName().startsWith("custom-executor-"));
            assertFalse(Thread.currentThread().isDaemon());
            randomSleep();
            return s.toUpperCase();
        }, executor);
        assertNull(cf.getNow(null));
        assertEquals("MSG", cf.join());
    }

    @Test public void test6() {
        StringBuffer sb = new StringBuffer();
        CompletableFuture.completedFuture("msg").thenAccept(s -> sb.append(s));
        assertEquals("msg", sb.toString());
    }

    @Test public void test7() {
        StringBuffer sb = new StringBuffer();
        CompletableFuture<Void> cf = CompletableFuture.completedFuture("msg").thenAcceptAsync(s -> sb.append(s));
        assertEquals("", sb.toString());
        cf.join();
        assertEquals("msg", sb.toString());
    }

    @Test public void test8() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("msg").thenApplyAsync(s -> {
            randomSleep();
            return s.toUpperCase();
        });
        CompletableFuture<String> handle = cf.handle((s, th) -> {
            return th != null ? "msg upon cancel" : "";
        });
        cf.completeExceptionally(new RuntimeException("complete exceptionally"));
        assertTrue("Was not completed exceptionally", cf.isCompletedExceptionally());
        try {
            cf.join();
            fail("Should have thrown an exception");
        } catch (CompletionException ex) {
            assertEquals("complete exceptionally", ex.getCause().getMessage());
        }

        assertEquals("msg upon cancel", handle.join());
    }

    @Test public void test9() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("msg").thenApplyAsync(s -> {
            randomSleep();
            return s.toUpperCase();
        });
        CompletableFuture cf2 = cf.exceptionally(throwable -> "canceled message");
        //等价于completeExceptionally(new CancellationException())
        assertTrue("Was not canceled", cf.cancel(true));
        assertTrue("Was not completed exceptionally", cf.isCompletedExceptionally());
        assertEquals("canceled message", cf2.join());
    }

    @Test(invocationCount = 3) public void test10() {
        String original = "msg";
        CompletableFuture<String> cf1 =
            CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedUpperCase(s));
        CompletableFuture<String> other =
            CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedLowerCase(s));
        //当cf1和other中的任意一个执行完成时，调用函数执行
        CompletableFuture<String> cf2 = cf1.applyToEither(other, s -> s + " from applyToEither");
        String result = cf2.join();
        System.out.println("result: " + result);
        assertTrue(result.endsWith(" from applyToEither"));
    }

    @Test(invocationCount = 3) public void test11() {
        String original = "msg";
        StringBuilder result = new StringBuilder();
        CompletableFuture<String> cf1 =
            CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedUpperCase(s));
        CompletableFuture<String> other =
            CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedLowerCase(s));
        //当cf1和other中的任意一个执行完成时，调用函数执行
        CompletableFuture<Void> cf = cf1.acceptEither(other, s -> result.append(s).append(" acceptEither"));
        cf.join();
        System.out.println("result: " + result.toString());
        assertTrue(result.toString().endsWith(" acceptEither"));
    }

    @Test public void test12() {
        String original = "msg";
        StringBuilder result = new StringBuilder();
        CompletableFuture<String> other = CompletableFuture.completedFuture(original).thenApply(String::toLowerCase);
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase)
            //两阶段执行完成后执行Runnable
            .runAfterBoth(other, () -> result.append("done"));
        System.out.println("result: " + result.toString());
        assertTrue(result.toString().endsWith("done"));
    }

    @Test public void test13() {
        String original = "msg ";
        StringBuilder result = new StringBuilder();
        CompletableFuture<String> other = CompletableFuture.completedFuture(original).thenApply(String::toLowerCase);
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase)
            //两阶段执行完成后执行
            .thenAcceptBoth(other, (s1, s2) -> result.append(s1).append(s2));
        System.out.println("result: " + result.toString());
        assertEquals("MSG msg ", result.toString());
    }

    @Test public void test14() {
        String original = "msg ";
        CompletableFuture<String> other =
            CompletableFuture.completedFuture(original).thenApply(s -> delayedLowerCase(s));
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original).thenApply(s -> delayedUpperCase(s))
            //两个future完成后，合并执行fn,用它来组合新的future
            .thenCombine(other, (s1, s2) -> s1 + s2);
        assertEquals("MSG msg ", cf.getNow(null));
    }

    @Test public void test15() {
        String original = "msg ";
        CompletableFuture<String> other =
            CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedLowerCase(s));
        CompletableFuture<String> cf =
            CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedUpperCase(s))
                .thenCombine(other, (s1, s2) -> s1 + s2);
        assertEquals("MSG msg ", cf.join());
    }

    @Test public void test16() {
        String original = "msg ";
        CompletableFuture<String> other =
            CompletableFuture.completedFuture(original).thenApply(s -> delayedLowerCase(s));
        CompletableFuture<String> upperCF =
            CompletableFuture.completedFuture(original).thenApply(s -> delayedUpperCase(s));
        //upperCF完成后将结果传递给upper,执行后面的任务，并组成新的future
        CompletableFuture<String> cf = upperCF.thenCompose(upper -> other.thenApply(s -> upper + s));
        assertEquals("MSG msg ", cf.join());
    }

    @Test(invocationCount = 3) public void test17() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture> futures =
            messages.stream().map(msg -> CompletableFuture.completedFuture(msg).thenApply(s -> delayedUpperCase(s)))
                .collect(Collectors.toList());
        CompletableFuture.anyOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((res, th) -> {
            if (th == null) {
                assertTrue(isUpperCase((String)res));
                result.append(res);
            }
        });
        assertTrue("Result was empty", result.length() > 0);
        System.out.println("result: " + result.toString());
    }

    @Test(invocationCount = 3) public void test17_1() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture> futures = messages.stream()
            .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(s -> delayedUpperCase(s)))
            .collect(Collectors.toList());
        CompletableFuture<Object> anyOf =
            CompletableFuture.anyOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((res, th) -> {
                if (th == null) {
                    assertTrue(isUpperCase((String)res));
                    result.append(res);
                }
            });
        anyOf.join();
        assertTrue("Result was empty", result.length() > 0);
        System.out.println("result: " + result.toString());
    }

    @Test public void test18() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture> futures =
            messages.stream().map(msg -> CompletableFuture.completedFuture(msg).thenApply(s -> delayedUpperCase(s)))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((v, th) -> {
            futures.forEach(cf -> {
                assertTrue(isUpperCase((String)cf.getNow(null)));
                result.append(cf.getNow(null));
            });
            result.append("done");
        });
        assertTrue("Result was empty", result.length() > 0);
        System.out.println("result: " + result.toString());
    }

    @Test public void test19() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture> futures = messages.stream()
            .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(s -> delayedUpperCase(s)))
            .collect(Collectors.toList());
        CompletableFuture allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
            .whenComplete((v, th) -> {
                futures.forEach(cf -> {
                    assertTrue(isUpperCase((String)cf.getNow(null)));
                    result.append(cf.getNow(null));
                });
                result.append("done");
            });
        allOf.join();
        assertTrue("Result was empty", result.length() > 0);
        System.out.println("result: " + result.toString());
    }

    private static boolean isUpperCase(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLowerCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static void randomSleep() {
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            // ...
        }
    }

    private static void sleepEnough() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ...
        }
    }

    private static String delayedUpperCase(String s) {
        randomSleep();
        return s.toUpperCase();
    }

    private static String delayedLowerCase(String s) {
        randomSleep();
        return s.toLowerCase();
    }

}
