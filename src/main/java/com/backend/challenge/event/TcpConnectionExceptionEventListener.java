package com.backend.challenge.event;

import com.backend.challenge.manager.ConnectionsManager;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpConnectionExceptionEvent;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;

/**
 * A custom listener for the <class>TcpConnectionExceptionEvent</class>.
 */
@Component
@AllArgsConstructor
public class TcpConnectionExceptionEventListener implements ApplicationListener<TcpConnectionExceptionEvent> {

    /**
     * The tcp connection manager.
     */
    private final ConnectionsManager connectionsManager;

    /**
     * Handles the TcpConnectionExceptionEvent by applying connectionsManager.closeConnection.
     *
     * @param event the occurred tcp connection exception event
     */
    @Override
    public void onApplicationEvent(TcpConnectionExceptionEvent event) {
        if (event.getCause() instanceof SocketTimeoutException) {
            connectionsManager.closeConnection(
                    ((TcpConnection) event.getSource()).getConnectionId()
            );
        }
    }

}
