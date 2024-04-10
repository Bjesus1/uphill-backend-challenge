package com.backend.challenge.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNioServerConnectionFactory;
import org.springframework.messaging.MessageChannel;

/**
 * The tcp server configuration
 */
@Configuration
public class TcpServerConfig {

    /**
     * The server port.
     */
    @Value("${tcp.server.port}")
    private int tcpServerPort;

    /**
     * The server connection timeout.
     */
    @Value("${tcp.server.timeout}")
    private int tcpServerTimeout;

    /**
     * Configures the server connection factory for tcp connections.
     *
     * @return the server connection factory.
     */
    @Bean
    public AbstractServerConnectionFactory serverConnectionFactory() {
        TcpNioServerConnectionFactory serverConnectionFactory = new TcpNioServerConnectionFactory(tcpServerPort);
        serverConnectionFactory.setUsingDirectBuffers(true);
        serverConnectionFactory.setSoTimeout(tcpServerTimeout);
        return serverConnectionFactory;
    }

    /**
     * The inbound channel.
     *
     * @return a new <class>DirectChannel</class> instance.
     */
    @Bean
    public MessageChannel inboundChannel() {
        return new DirectChannel();
    }

    /**
     * The tcp inbound configuration.
     *
     * @param serverConnectionFactory the server connection factory.
     * @param inboundChannel          the inbound channel to consume received input.
     * @return a new <class>TcpInboundGateway</class> instance.
     */
    @Bean
    public TcpInboundGateway inboundGateway(AbstractServerConnectionFactory serverConnectionFactory,
                                            @Qualifier("inboundChannel") MessageChannel inboundChannel) {
        TcpInboundGateway tcpInboundGateway = new TcpInboundGateway();
        tcpInboundGateway.setConnectionFactory(serverConnectionFactory);
        tcpInboundGateway.setRequestChannel(inboundChannel);
        return tcpInboundGateway;
    }

}
