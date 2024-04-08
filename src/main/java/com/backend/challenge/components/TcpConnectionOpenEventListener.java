package com.backend.challenge.components;

import com.backend.challenge.ConnectionsManager;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TcpConnectionOpenEventListener implements ApplicationListener<TcpConnectionOpenEvent> {

    @Override
    public void onApplicationEvent(TcpConnectionOpenEvent event) {
        ConnectionsManager.openConnection((TcpConnection) event.getSource());
    }
}
