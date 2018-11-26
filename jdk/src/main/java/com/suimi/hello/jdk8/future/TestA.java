/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.jdk8.future;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author suimi
 * @date 2018/5/24
 */
public class TestA {

    @Test public void test2() {
        new TestA().print();
    }

    public CompletableFuture<TestA> print() {
        CompletableFuture<TestA> future = CompletableFuture.completedFuture(null);
        CompletableFuture<Void> f1 = future.thenRun(this::print1);
        CompletableFuture<TestA> f2 = f1.thenApply(t -> {
            System.out.println("print Apply");
            return this;
        }).exceptionally(t -> {
            t.printStackTrace();
            return this;
        });
        return f2;
    }

    public void print1() {
        System.out.println("print A");
        throw new RuntimeException("test");
    }

    @Test
    public void test3() throws ExecutionException, InterruptedException {
        /**获取单词，并且去重**/
        List<String> list = Arrays.asList("hello welcome", "world hello", "hello world",
            "hello world welcome");

        //map和flatmap的区别
        list.stream().map(item -> Arrays.stream(item.split(" "))).distinct().collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("---------- ");
        list.stream().flatMap(item -> Arrays.stream(item.split(" "))).distinct().collect(Collectors.toList()).forEach(System.out::println);

        //实际上返回的类似是不同的
        List<Stream<String>> listResult = list.stream().map(item -> Arrays.stream(item.split(" "))).distinct().collect(Collectors.toList());
        List<String> listResult2 = list.stream().flatMap(item -> Arrays.stream(item.split(" "))).distinct().collect(Collectors.toList());

        System.out.println("---------- ");

        //也可以这样
        list.stream().map(item -> item.split(" ")).flatMap(Arrays::stream).distinct().collect(Collectors.toList()).forEach(System.out::println);

        System.out.println("================================================");

        /**相互组合**/
        List<String> list2 = Arrays.asList("hello", "hi", "你好");
        List<String> list3 = Arrays.asList("zhangsan", "lisi", "wangwu", "zhaoliu");

        list2.stream().map(item -> list3.stream().map(item2 -> item + " " + item2)).collect(Collectors.toList()).forEach(System.out::println);
        list2.stream().flatMap(item -> list3.stream().map(item2 -> item + " " + item2)).collect(Collectors.toList()).forEach(System.out::println);

        //实际上返回的类似是不同的
        List<Stream<String>> list2Result = list2.stream().map(item -> list3.stream().map(item2 -> item + " " + item2)).collect(Collectors.toList());
        List<String> list2Result2 = list2.stream().flatMap(item -> list3.stream().map(item2 -> item + " " + item2)).collect(Collectors.toList());

    }

}
