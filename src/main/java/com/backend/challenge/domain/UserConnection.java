package com.backend.challenge.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.integration.ip.tcp.connection.TcpConnection;

import java.time.Instant;

@Builder
@Getter
@Setter
public class UserConnection {

    private String name;

    private Instant startConnection;

    private TcpConnection tcpConnection;

}
