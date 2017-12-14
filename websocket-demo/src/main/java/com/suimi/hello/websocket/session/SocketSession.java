/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.websocket.session;

import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author suimi
 * @date 2017-12-13
 */
@Entity
@Data
@NoArgsConstructor
public class SocketSession extends AbstractPersistable<Long> {
    private int socketId;

    private String sessionId;

}
