package com.backend.challenge.service;


import com.backend.challenge.ConnectionsManager;
import com.backend.challenge.graph.DirectGraph;
import com.backend.challenge.graph.Node;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.backend.challenge.constants.ServerConstant.endRegex;
import static com.backend.challenge.constants.ServerConstant.errorMessage;
import static com.backend.challenge.constants.ServerConstant.nameRegex;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final ConnectionsManager connectionsManager;

    private final DirectGraph directGraph;

    @Override
    public byte[] processMessage(byte[] message, String connectionId) {
        String messageContent = new String(message);

        if (!StringUtils.hasText(messageContent)) {
            return errorMessage.getBytes();
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
            return (shortestPathWeight == -1 ? "ERROR: NODE NOT FOUND" : String.valueOf(shortestPathWeight)).getBytes();
        }

        if (messageContent.startsWith("CLOSER THAN")) {
            LOGGER.info(messageContent);
            String[] args = messageContent.replace("CLOSER THAN ", "").split(" ");
            Set<Node> closerThanList = directGraph.findNodesCloserThanWeight(Integer.parseInt(args[0]), args[1]);

            if (closerThanList == null) return errorMessage.getBytes();

            return (
                    closerThanList.isEmpty() ? "" : closerThanList.stream()
                            .map(Node::getName)
                            .sorted()
                            .filter(name -> !name.equals(args[1]))
                            .collect(Collectors.joining(",")
                            )
            ).getBytes();
        }

        return errorMessage.getBytes();

    }

    private byte[] handlePhaseOne(String messageContent, String connectionId) {
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

    private byte[] handlePhaseTwo(String messageContent) {
        String[] command = messageContent.split(" ");
        String operation = command[0], domain = command[1], originNode = command[2], destinyNode;
        int weight;

        if (domain.equals("NODE")) {
            return switch (operation) {
                case "ADD" ->
                        (directGraph.addNode(originNode) != null ? "NODE ADDED" : "ERROR: NODE ALREADY EXISTS").getBytes();
                case "REMOVE" ->
                        (directGraph.removeNode(originNode) != null ? "NODE REMOVED" : "ERROR: NODE NOT FOUND").getBytes();
                default -> errorMessage.getBytes();
            };
        }

        if (domain.equals("EDGE")) {
            switch (operation) {
                case "ADD":
                    destinyNode = command[3];
                    weight = Integer.parseInt(command[4]);
                    return (directGraph.addEdge(originNode, destinyNode, weight) != null ? "EDGE ADDED" : "ERROR: NODE NOT FOUND").getBytes();
                case "REMOVE":
                    destinyNode = command[3];
                    return (directGraph.removeEdge(originNode, destinyNode) != null ? "EDGE REMOVED" : "ERROR: NODE NOT FOUND").getBytes();
                default:
                    return errorMessage.getBytes();
            }

        }

        return errorMessage.getBytes();

    }

}