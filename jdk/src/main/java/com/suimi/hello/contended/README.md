---
layout: post
title: 伪共享
tags: ["java","伪共享"]
categories: ["java"]
---
* TOC
{:toc}

# 什么是伪共享
缓存系统中是以缓存行（cache line）为单位存储的。缓存行是2的整数幂个连续字节，一般为32-256个字节。最常见的缓存行大小是64个字节。当多线程修改互相独立的变量时，如 果这些变量共享同一个缓存行，就会无意中影响彼此的性能，这就是伪共享。缓存行上的写竞争是运行在SMP系统中并行线程实现可伸缩性最重要的限制因素。有 人将伪共享描述成无声的性能杀手，因为从代码中很难看清楚是否会出现伪共享。

解决伪共享的办法是使用缓存行填充，使一个对象占用的内存大小刚好为64bytes或它的整数倍，这样就保证了一个缓存行里不会有多个对象。

JAVA 8中添加了一个@Contended的注解，添加这个的注解，将会在自动进行缓存行填充。执行时，必须加上虚拟机参数-XX:-RestrictContended，@Contended注释才会生效。


# Disruptor 缓存行填充

```java
class LhsPadding {
    protected long p1, p2, p3, p4, p5, p6, p7;
}

class Value extends LhsPadding {
    protected volatile long value;
}

class RhsPadding extends Value {
    protected long p9, p10, p11, p12, p13, p14, p15;
}
```

1. 以目前主流64字节（也即8个long）缓存行大小处理，如果是128字节缓存行，依旧存在伪共享问题
2. 解决方式是在value头尾各填充7个long，以避免value与其他数据共处一缓存行

参考:
- [伪共享和缓存行填充，从Java 6, Java 7 到Java 8](http://www.cnblogs.com/Binhua-Liu/p/5620339.html)
- [Java8的伪共享和缓存行填充--@Contended注释](https://www.cnblogs.com/Binhua-Liu/p/5623089.html)
- [Disruptor 缓冲行填充的进一步解释](https://www.jianshu.com/p/e1a1b950fc4a)