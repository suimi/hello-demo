/*
 * Copyright (c) 2013-2015, 成都中联信通科技股份有限公司
 * Created by lichengcai on 2017-08-28.
 */
package com.suimi.hello.nio;

import java.nio.ByteBuffer;

import org.junit.Test;

public class NIOTEST {

    @Test
    public void testBuffer() {
        ByteBuffer bf = ByteBuffer.allocate(2);
        for (Integer i = 0; i < 3; i++) {
            bf.put(i.byteValue());
        }
        System.out.println(bf.toString());
    }

}
