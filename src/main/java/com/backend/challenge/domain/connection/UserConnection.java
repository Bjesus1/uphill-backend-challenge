package com.backend.challenge.domain.connection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.integration.ip.tcp.connection.TcpConnection;

import java.time.Instant;

/**
 * User connection representation class
 */
@Builder
@Getter
@Setter
public class UserConnection {

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The time when the connection was established.
     */
    private Instant startConnection;

    /**
     * The connection reference.
     */
    private TcpConnection tcpConnection;

}
