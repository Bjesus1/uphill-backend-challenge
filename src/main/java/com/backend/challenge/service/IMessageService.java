package com.backend.challenge.service;

/**
 * Interface for the message service.
 */
public interface IMessageService {

    /**
     * Processes a received byte array and connection identification.
     *
     * @param message      the received message of a tcp connection.
     * @param connectionId the tcp connection identification.
     * @return an output message for the connection.
     */
    byte[] processMessage(byte[] message, String connectionId);

}
