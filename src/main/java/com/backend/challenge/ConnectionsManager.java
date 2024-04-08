package com.backend.challenge;

import com.backend.challenge.domain.UserConnection;
import lombok.Getter;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class ConnectionsManager {

    private final Map<String, UserConnection> activeConnections = new HashMap<>();

    public void openConnection(TcpConnection tcpConnection) {
        String[] connectionIdSplit = tcpConnection.getConnectionId().split(":");
        String message = "HI, I AM " + connectionIdSplit[connectionIdSplit.length - 1];
        putUserConnection(tcpConnection.getConnectionId(),
                UserConnection.builder().name("").startConnection(Instant.now()).tcpConnection(tcpConnection).build()
        );
        tcpConnection.send(MessageBuilder.withPayload(message).build());
    }

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

    public UserConnection getUserConnectionByConnectionId(String connectionId) {
        return activeConnections.getOrDefault(connectionId, UserConnection.builder().build());
    }

    public void putUserConnection(String connectionId, UserConnection userConnection) {
        activeConnections.put(connectionId, userConnection);
    }

    public void updateUserConnectionName(String connectionId, String name) {
        activeConnections.get(connectionId).setName(name);
    }


}
