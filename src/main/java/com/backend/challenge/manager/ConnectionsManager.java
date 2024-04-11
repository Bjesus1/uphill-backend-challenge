package com.backend.challenge.manager;

import com.backend.challenge.domain.connection.UserConnection;
import lombok.Getter;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager of tcp connections.
 */
@Component
public class ConnectionsManager {

    /**
     * A map of the active connections.
     */
    private final Map<String, UserConnection> activeConnections = new HashMap<>();

    /**
     * Opens the connection started by the client and presents a welcome message.
     * Adds this connection to the active connections map.
     *
     * @param tcpConnection the tcp connection to be welcomed and stored.
     */
    public void openConnection(TcpConnection tcpConnection) {
        String[] connectionIdSplit = tcpConnection.getConnectionId().split(":");
        String message = "HI, I AM " + connectionIdSplit[connectionIdSplit.length - 1];
        putUserConnection(tcpConnection.getConnectionId(),
                UserConnection.builder().name("").startConnection(Instant.now()).tcpConnection(tcpConnection).build()
        );
        tcpConnection.send(MessageBuilder.withPayload(message).build());
    }

    /**
     * Closes a connection with the received identification and removes it from the active connections map.
     *
     * @param connectionId the connection identification.
     */
    public void closeConnection(String connectionId) {
        TcpConnection connection = activeConnections.get(connectionId).getTcpConnection();
        if (connection.isOpen()) {
            UserConnection userConnection = getUserConnectionByConnectionId(connection.getConnectionId());
            String message = String.format("BYE %s, WE SPOKE FOR %d MS",
                    userConnection.getName(),
                    Duration.between(userConnection.getStartConnection(), Instant.now()).toMillis()
            );
            connection.send(MessageBuilder.withPayload(message).build());
            connection.close();
        }
    }

    /**
     * Updates the name of a user connection.
     *
     * @param connectionId the connection identification.
     * @param name         the new name value.
     */
    public void updateUserConnectionName(String connectionId, String name) {
        activeConnections.getOrDefault(connectionId, UserConnection.builder().build()).setName(name);
    }

    /**
     * Gets the user connection of the received connection identification.
     *
     * @param connectionId the connection identification.
     * @return the <class>UserConnection</class> instance associated with the connection identification. If none exists,
     * returns a new instance.
     */
    private UserConnection getUserConnectionByConnectionId(String connectionId) {
        return activeConnections.getOrDefault(connectionId, UserConnection.builder().build());
    }

    /**
     * Adds a new entry of the received arguments.
     *
     * @param connectionId   the connection identification
     * @param userConnection the <class>UserConnection</class>instance associated with the connection identificaiton.
     */
    private void putUserConnection(String connectionId, UserConnection userConnection) {
        activeConnections.put(connectionId, userConnection);
    }

}
