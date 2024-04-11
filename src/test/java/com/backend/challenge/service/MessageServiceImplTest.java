package com.backend.challenge.service;

import com.backend.challenge.domain.graph.DirectGraph;
import com.backend.challenge.manager.ConnectionsManager;
import org.junit.jupiter.api.Test;

import static com.backend.challenge.constant.ServerConstant.ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MessageServiceImplTest {

    private final MessageServiceImpl messageService;

    public MessageServiceImplTest() {
        this.messageService = new MessageServiceImpl(
                new ConnectionsManager(),
                new DirectGraph()
        );
    }

    @Test
    void testPhaseOneWithNoText(){
        // Arrange
        byte[] expResult = ERROR_MESSAGE.getBytes();
        byte[] messageToSend = "".getBytes();
        byte[] result;

        // Act
        result = messageService.processMessage(messageToSend, null);

        // Assert
        assertArrayEquals(expResult, result);
    }

    @Test
    void testPhaseOneWithBadText(){
        // Arrange
        byte[] expResult = ERROR_MESSAGE.getBytes();
        byte[] messageToSend = "BLACK MAGIC".getBytes();
        byte[] result;

        // Act
        result = messageService.processMessage(messageToSend, null);

        // Assert
        assertArrayEquals(expResult, result);
    }

    @Test
    void testPhaseOneWithMatchName(){
        // Arrange
        String name = "SonGoku";
        byte[] expResult = ("HI " + name).getBytes();
        byte[] messageToSend = ("HI, I AM " + name).getBytes();
        byte[] result;

        // Act
        result = messageService.processMessage(messageToSend, null);

        // Assert
        assertArrayEquals(expResult, result);
    }

    @Test
    void testPhaseOneWithWrongName(){
        // Arrange
        String name = "Son Goku";
        byte[] expResult = ERROR_MESSAGE.getBytes();
        byte[] messageToSend = ("HI, I AM " + name).getBytes();
        byte[] result;

        // Act
        result = messageService.processMessage(messageToSend, null);

        // Assert
        assertArrayEquals(expResult, result);
    }

    @Test
    void testPhaseOneWithInvalidMessage(){
        // Arrange
        String name = "SonGoku";
        byte[] expResult = ERROR_MESSAGE.getBytes();
        byte[] messageToSend = ("HI, MY NAME IS " + name).getBytes();
        byte[] result;

        // Act
        result = messageService.processMessage(messageToSend, null);

        // Assert
        assertArrayEquals(expResult, result);
    }


}
