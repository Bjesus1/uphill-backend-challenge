package com.backend.challenge.service;


import com.backend.challenge.ConnectionsManager;
import com.backend.challenge.domain.graph.DirectGraph;
import com.backend.challenge.domain.graph.Node;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

import static com.backend.challenge.constant.ServerConstant.ADD_OPERATION;
import static com.backend.challenge.constant.ServerConstant.EDGE_ADDED;
import static com.backend.challenge.constant.ServerConstant.EDGE_CONST;
import static com.backend.challenge.constant.ServerConstant.EDGE_REMOVED;
import static com.backend.challenge.constant.ServerConstant.NODE_ADDED;
import static com.backend.challenge.constant.ServerConstant.NODE_ALREADY_EXISTS;
import static com.backend.challenge.constant.ServerConstant.NODE_CONST;
import static com.backend.challenge.constant.ServerConstant.NODE_NOT_FOUND;
import static com.backend.challenge.constant.ServerConstant.NODE_REMOVED;
import static com.backend.challenge.constant.ServerConstant.REMOVE_OPERATION;
import static com.backend.challenge.constant.ServerConstant.TERMINATE_REGEX;
import static com.backend.challenge.constant.ServerConstant.ERROR_MESSAGE;
import static com.backend.challenge.constant.ServerConstant.NAME_REGEX;

/**
 * The implementation class of the IMessageService.
 * Responsible to handle messages.
 */
@Service
@AllArgsConstructor
public class MessageServiceImpl implements IMessageService {

    /**
     * The tcp connections manager.
     */
    private final ConnectionsManager connectionsManager;

    /**
     * The direct graph.
     */
    private final DirectGraph directGraph;

    /**
     * Processes a received byte array and connection identification, handling all 6 phases requirements.
     *
     * @param message      the received message of a tcp connection.
     * @param connectionId the tcp connection identification.
     * @return an output message for the connection.
     */
    @Override
    public byte[] processMessage(byte[] message, String connectionId) {
        String messageContent = new String(message);

        if (!StringUtils.hasText(messageContent)) {
            return ERROR_MESSAGE.getBytes();
        }

        if (messageContent.startsWith("HI, ") || messageContent.equals("BYE MATE!")) {
            return handlePhaseOne(messageContent, connectionId);
        }

        if (messageContent.startsWith("ADD ") || messageContent.startsWith("REMOVE ")) {
            return handlePhaseTwo(messageContent);
        }

        if (messageContent.startsWith("SHORTEST PATH")) {
            String[] args = messageContent.replace("SHORTEST PATH ", "").split(" ");
            int shortestPathWeight = directGraph.findShortestPath(args[0], args[1]);
            return (shortestPathWeight == -1 ? NODE_NOT_FOUND : String.valueOf(shortestPathWeight)).getBytes();
        }

        if (messageContent.startsWith("CLOSER THAN")) {
            String[] args = messageContent.replace("CLOSER THAN ", "").split(" ");
            Set<Node> closerThanList = directGraph.findNodesCloserThanWeight(Integer.parseInt(args[0]), args[1]);

            if (closerThanList == null) return ERROR_MESSAGE.getBytes();

            return (
                    closerThanList.isEmpty() ? "" : closerThanList.stream()
                            .map(Node::getName)
                            .sorted()
                            .filter(name -> !name.equals(args[1]))
                            .collect(Collectors.joining(",")
                            )
            ).getBytes();
        }

        return ERROR_MESSAGE.getBytes();

    }

    /**
     * Handles phase 1 requirements.
     *
     * @param messageContent the received message input as text.
     * @param connectionId   the connection identification.
     * @return the phase 1 requirements desired response based on the provided conditions.
     */
    private byte[] handlePhaseOne(String messageContent, String connectionId) {
        if (messageContent.matches(NAME_REGEX)) {
            String name = messageContent.replace("HI, I AM", "").trim();
            String responseContent = String.format("HI %s", name);
            connectionsManager.updateUserConnectionName(connectionId, name);
            return responseContent.getBytes();
        }

        if (messageContent.matches(TERMINATE_REGEX)) {
            connectionsManager.closeConnection(connectionId);
        }

        return ERROR_MESSAGE.getBytes();

    }

    /**
     * Handles phase 2 requirements.
     *
     * @param messageContent the received message input as text.
     * @return the phase 2 requirements desired response based on the provided conditions.
     */
    private byte[] handlePhaseTwo(String messageContent) {
        String[] command = messageContent.split(" ");
        String operation = command[0];
        String domain = command[1];
        String originNodeName = command[2];
        String destinyNode;
        int weight;

        if (domain.equals(NODE_CONST)) {
            return switch (operation) {
                case ADD_OPERATION ->
                        (directGraph.addNode(originNodeName) != null ? NODE_ADDED : NODE_ALREADY_EXISTS).getBytes();
                case REMOVE_OPERATION ->
                        (directGraph.removeNode(originNodeName) != null ? NODE_REMOVED : NODE_NOT_FOUND).getBytes();
                default -> ERROR_MESSAGE.getBytes();
            };
        }

        if (domain.equals(EDGE_CONST)) {
            switch (operation) {
                case ADD_OPERATION:
                    destinyNode = command[3];
                    weight = Integer.parseInt(command[4]);
                    return (directGraph.addEdge(originNodeName, destinyNode, weight) != null ? EDGE_ADDED : NODE_NOT_FOUND).getBytes();
                case REMOVE_OPERATION:
                    destinyNode = command[3];
                    return (directGraph.removeEdge(originNodeName, destinyNode) != null ? EDGE_REMOVED : NODE_NOT_FOUND).getBytes();
                default:
                    return ERROR_MESSAGE.getBytes();
            }

        }

        return ERROR_MESSAGE.getBytes();

    }

}