/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.reactor;

import org.reactivestreams.Subscription;
import org.testng.annotations.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author suimi
 * @date 2018/11/29
 */
public class ReactorExample {

    @Test
    public void test1() {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5, 6);
        flux.subscribe(System.out::println);
        System.out.println("flux");
        Mono.just(10).subscribe(System.out::println);

        flux.subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("Completed!"));

        Mono.error(new Exception("some error")).subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("Completed!")
        );
    }

    private Flux<Integer> generateFluxFrom1To6() {
        return Flux.just(1, 2, 3, 4, 5, 6);
    }
    private Mono<Integer> generateMonoWithError() {
        return Mono.error(new Exception("some error"));
    }

    @Test public void testDebug() {
        StepVerifier.create(generateFluxFrom1To6())
            //expectNext用于测试下一个期望的数据元素
            .expectNext(1, 2, 3, 4, 5, 6)
            //expectComplete用于测试下一个元素是否为完成信号
            .expectComplete()
            .verify();
        StepVerifier.create(generateMonoWithError())
            //expectErrorMessage用于校验下一个元素是否为错误信号
            .expectErrorMessage("some error")
            .verify();
    }

    @Test public void testMap() {
        Flux<Integer> mapResult = Flux.range(1, 10).map(i -> i * i);
        mapResult.subscribe(System.out::println);
        StepVerifier.create(mapResult)
            .expectNext(1,4,9,16,25,36,49,64,81,100)
            .expectComplete()
            .verify();
    }

    @Test public void testFlatMap() {
        String[] one = new String[]{"one","two","three"};
        String[] two = new String[]{"1","2","3"};
        Flux<String> flatMapFlux = Flux.just(one, two).flatMap(a -> Flux.fromArray(a)).delayElements(Duration.ofMillis(100));
        flatMapFlux.subscribe(System.out::println);

        System.out.println("==============verify=========");
        //doOnNext方法是“偷窥式”的方法，不会消费数据流
        StepVerifier.create(flatMapFlux.doOnNext(System.out::println))
            .expectNextCount(6)
            .verifyComplete();
        System.out.println("==============verify end =========");
        Flux.just(one, two).map(a -> Flux.fromArray(a)).subscribe(System.out::println);
    }

    @Test public void testFilter() {
        StepVerifier.create(Flux.range(1,6)
            .filter(i->i%2==1)
            .map(i->i*i)
            .doOnNext(System.out::println))
            .expectNext(1,9,25)
            .verifyComplete();
    }

    @Test public void testZip() {
        Flux<Tuple2<Integer, String>> zip = Flux.zip(Flux.range(1, 6), Flux.just("one", "two", "three","four"));
        zip.subscribe(z -> z.forEach(System.out::println));
    }

    @Test public void testZip2() throws InterruptedException {
        String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.";
        ;  // 1

        CountDownLatch countDownLatch = new CountDownLatch(1);  // 2
        Flux.zip(
            Flux.fromArray(desc.split("\\s+")),
            //interval声明一个每200ms发出一个元素的long数据流
            Flux.interval(Duration.ofMillis(200)))  // 3
            .subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);    // 4
        countDownLatch.await(10, TimeUnit.SECONDS);     // 5
    }

    private String getStringSync() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, Reactor!";
    }

    @Test
    public void testSyncToAsync() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(() -> getStringSync())    // 1
            .subscribeOn(Schedulers.elastic())  // 2
            .subscribe(System.out::println, null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testBackpressure() {
        Flux.range(1, 6)    // 1
            .doOnRequest(n -> System.out.println("Request " + n + " values..."))    // 2
            .subscribe(new BaseSubscriber<Integer>() {  // 3
                @Override
                protected void hookOnSubscribe(Subscription subscription) { // 4
                    System.out.println("Subscribed and make a request...");
                    request(1); // 5
                }

                @Override
                protected void hookOnNext(Integer value) {  // 6
                    try {
                        TimeUnit.SECONDS.sleep(1);  // 7
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Get value [" + value + "]");    // 8
                    request(1); // 9
                }
            });
    }
}
