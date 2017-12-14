/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author suimi
 * @date 2017-12-12
 */
@Data
@AllArgsConstructor
@ToString
public class Message {

    private int index;

    private String msg;
}
