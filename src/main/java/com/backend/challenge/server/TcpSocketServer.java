package com.backend.challenge.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Bootstrapper class for the tcp server.
 */
@SpringBootApplication(scanBasePackages = "com")
public class TcpSocketServer {

    /**
     * Bootstrap the tcp server.
     *
     * @param args list of provided args
     */
    public static void main(String[] args) {
        SpringApplication.run(TcpSocketServer.class, args);
    }

}
