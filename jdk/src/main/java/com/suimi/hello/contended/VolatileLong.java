/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.contended;

import sun.misc.Contended;

/**
 * @author suimi
 * @date 2018-01-18
 */
//@Contended
public class VolatileLong {
    public volatile long value = 0L;
}
