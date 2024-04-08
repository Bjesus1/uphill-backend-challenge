package com.backend.challenge.endpoints;


import com.backend.challenge.service.IMessageService;
import lombok.AllArgsConstructor;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;

@MessageEndpoint
@AllArgsConstructor
public class TcpServerEndpoint {

    private final IMessageService messageService;

    @ServiceActivator(inputChannel = "inboundChannel")
    public byte[] process( byte[] message, @Header("ip_connectionId") String connectionId) {
        return messageService.processMessage(message, connectionId);
    }
}
