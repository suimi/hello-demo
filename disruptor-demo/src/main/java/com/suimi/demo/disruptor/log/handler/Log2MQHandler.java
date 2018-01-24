/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.disruptor.log.handler;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.suimi.demo.disruptor.log.LogInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suimi
 * @date 2018-01-15
 */
@Slf4j
public class Log2MQHandler implements EventHandler<LogInfo>, WorkHandler<LogInfo> {
    @Override
    public void onEvent(LogInfo event, long sequence, boolean endOfBatch) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Log to MQ:{}, sequence:{}, endOfBatch:{}", event, sequence, endOfBatch);
        }
    }

    @Override
    public void onEvent(LogInfo event) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Log to MQ:{}", event);
        }
    }
}
