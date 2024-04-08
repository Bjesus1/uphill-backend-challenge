package com.backend.challenge.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com")
public class TcpSocketServer {

    public static void main(String[] args) {
        SpringApplication.run(TcpSocketServer.class, args);
    }

}
