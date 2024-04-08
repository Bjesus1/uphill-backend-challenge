package com.backend.challenge.components;

import com.backend.challenge.ConnectionsManager;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpConnectionExceptionEvent;
import org.springframework.stereotype.Component;

@Component
public class TcpConnectionExceptionEventListener implements ApplicationListener<TcpConnectionExceptionEvent> {

    @Override
    public void onApplicationEvent(TcpConnectionExceptionEvent event) {
        ConnectionsManager.closeConnection(
                ((TcpConnection) event.getSource()).getConnectionId()
        );
    }

}
