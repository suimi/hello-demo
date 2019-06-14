package com.suimi.hello.contended;

import sun.misc.Contended;

/**
 * @author suimi
 * @date 2019/6/14
 */
public class ContendedValueTest {

     ContendedValue value = new ContendedValue(10L);

    @Contended private  int x;

    private  int y = 0;

    @Contended private  int z;

    public static void main(String[] args) {

        new ContendedValueTest();
    }
}
