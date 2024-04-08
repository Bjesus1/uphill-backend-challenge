package com.backend.challenge.service;

public interface IMessageService {

    byte[] processMessage(byte[] message, String connectionId);

}
