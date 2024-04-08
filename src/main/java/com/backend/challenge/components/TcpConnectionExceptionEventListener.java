package com.backend.challenge.components;

import com.backend.challenge.ConnectionsManager;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpConnectionExceptionEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TcpConnectionExceptionEventListener implements ApplicationListener<TcpConnectionExceptionEvent> {

    private final ConnectionsManager connectionsManager;

    @Override
    public void onApplicationEvent(TcpConnectionExceptionEvent event) {
        connectionsManager.closeConnection(
                ((TcpConnection) event.getSource()).getConnectionId()
        );
    }

}
