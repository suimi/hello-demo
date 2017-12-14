/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.websocket.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author suimi
 * @date 2017-12-13
 */
@Repository
public interface SocketSessionRepository extends JpaRepository<SocketSession, Long> {

}
