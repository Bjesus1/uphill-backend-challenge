package com.backend.challenge.service;


import com.backend.challenge.ConnectionsManager;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.backend.challenge.constants.ServerConstant.endRegex;
import static com.backend.challenge.constants.ServerConstant.errorMessage;
import static com.backend.challenge.constants.ServerConstant.nameRegex;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements IMessageService {

    //private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final ConnectionsManager connectionsManager;

    @Override
    public byte[] processMessage(byte[] message, String connectionId) {
        String messageContent = new String(message);

        if (!StringUtils.hasText(messageContent)){
            return errorMessage.getBytes();
        }

        if (messageContent.matches(nameRegex)) {
            String name = messageContent.replace("HI, I AM", "").trim();
            String responseContent = String.format("HI %s", name);
            connectionsManager.updateUserConnectionName(connectionId, name);
            return responseContent.getBytes();
        }

        if (messageContent.matches(endRegex)) {
            connectionsManager.closeConnection(connectionId);
        }

        return errorMessage.getBytes();

    }

}
