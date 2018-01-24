/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.disruptor.log;

import org.springframework.boot.logging.LogLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author suimi
 * @date 2018-01-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInfo {

    private LogLevel level;

    private String message;
}
