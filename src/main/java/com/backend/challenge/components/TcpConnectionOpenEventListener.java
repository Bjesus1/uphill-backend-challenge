package com.backend.challenge.components;

import com.backend.challenge.ConnectionsManager;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@AllArgsConstructor
public class TcpConnectionOpenEventListener implements ApplicationListener<TcpConnectionOpenEvent> {

    private final ConnectionsManager connectionsManager;

    @Override
    public void onApplicationEvent(TcpConnectionOpenEvent event) {
        connectionsManager.openConnection((TcpConnection) event.getSource());
    }
}
