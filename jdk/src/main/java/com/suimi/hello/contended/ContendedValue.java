package com.suimi.hello.contended;

import sun.corba.Bridge;

/**
 * @author suimi
 * @date 2019/6/14
 */

class LhsPadding {
    protected long p1, p2, p3, p4, p5, p6, p7;
}

class Value extends LhsPadding {
    protected volatile long value;
}

class RhsPadding extends Value {
    protected long p9, p10, p11, p12, p13, p14, p15;
}

public class ContendedValue extends RhsPadding {

    
    public ContendedValue(long value) {
        this.value = value;
    }

}
