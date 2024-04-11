package com.backend.challenge.event;

import com.backend.challenge.manager.ConnectionsManager;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.stereotype.Component;

/**
 * A custom listener for the <class>TcpConnectionOpenEvent</class>.
 */
@Component
@AllArgsConstructor
public class TcpConnectionOpenEventListener implements ApplicationListener<TcpConnectionOpenEvent> {

    /**
     * The tcp connection manager.
     */
    private final ConnectionsManager connectionsManager;

    /**
     * Handles the TcpConnectionExceptionEvent by applying connectionsManager.openConnection.
     *
     * @param event the occurred tcp connection exception event
     */
    @Override
    public void onApplicationEvent(TcpConnectionOpenEvent event) {
        connectionsManager.openConnection((TcpConnection) event.getSource());
    }
    
}
