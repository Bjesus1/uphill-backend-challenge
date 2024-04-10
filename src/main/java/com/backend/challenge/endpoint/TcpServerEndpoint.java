package com.backend.challenge.endpoint;


import com.backend.challenge.service.IMessageService;
import lombok.AllArgsConstructor;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;

/**
 * The message endpoint for the tcp server connections.
 */
@MessageEndpoint
@AllArgsConstructor
public class TcpServerEndpoint {

    /**
     * The message service, responsible to handle incoming messages.
     */
    private final IMessageService messageService;

    /**
     * Method activated as the inbound channel handler.
     *
     * @param message      the connection input.
     * @param connectionId the connection identification.
     * @return an output response for the connection.
     */
    @ServiceActivator(inputChannel = "inboundChannel")
    public byte[] process(byte[] message, @Header("ip_connectionId") String connectionId) {
        return messageService.processMessage(message, connectionId);
    }
}
