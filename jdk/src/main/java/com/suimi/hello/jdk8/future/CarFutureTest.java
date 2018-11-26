/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.jdk8.future;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author suimi
 * @date 2018/11/26
 */
public class CarFutureTest {

    @Test public void notFuture() {
        long start = System.currentTimeMillis();

        List<Car> cars = cars();
        cars.forEach(car -> {
            float rating = rating(car.manufacturerId);
            car.setRating(rating);
        });

        cars.forEach(System.out::println);

        long end = System.currentTimeMillis();

        System.out.println("Took " + (end - start) + " ms.");
    }

    @Test public void testFuture() {
        long start = System.currentTimeMillis();
        CompletableFuture.supplyAsync(() -> cars()).thenCompose(cars -> {
            List<CompletableFuture<Car>> futures = cars.stream()
                .map(car -> CompletableFuture.supplyAsync(() -> rating(car.manufacturerId)).thenApplyAsync(r -> {
                    car.setRating(r);
                    return car;
                })).collect(Collectors.toList());
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
        }).whenComplete((cars, th) -> {
            if (th == null) {
                cars.forEach(System.out::println);
            } else {
                throw new RuntimeException(th);
            }
        }).toCompletableFuture().join();
        long end = System.currentTimeMillis();
        System.out.println("Took " + (end - start) + " ms.");
    }

    static float rating(int manufacturer) {
        try {
            simulateDelay();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        switch (manufacturer) {
            case 2:
                return 4f;
            case 3:
                return 4.1f;
            case 7:
                return 4.2f;
            default:
                return 5f;
        }
    }

    static List<Car> cars() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car(1, 3, "Fiesta", 2017));
        carList.add(new Car(2, 7, "Camry", 2014));
        carList.add(new Car(3, 2, "M2", 2008));
        return carList;
    }

    private static void simulateDelay() throws InterruptedException {
        Thread.sleep(5000);
    }
}
