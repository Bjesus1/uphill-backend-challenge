package com.backend.challenge.graph;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

@Component
@NoArgsConstructor
public class DirectGraph {

    Map<String, Node> graphNodes = new HashMap<>();

    public Node addNode(String name) {
        if (graphNodes.containsKey(name)) {
            return null;
        }
        Node node = Node.builder().name(name).neighbours(new HashSet<>()).build();
        graphNodes.putIfAbsent(name, node);
        return node;
    }

    public Edge addEdge(String origin, String destination, int weight) {
        Node originNode = graphNodes.get(origin);
        Node destinationNode = graphNodes.get(destination);
        if (originNode == null || destinationNode == null) {
            return null;
        }
        Edge edge = Edge.builder().destinyNode(destinationNode).weight(weight).build();
        return originNode.addNeighbour(edge) ? edge : null;
    }

    public Node removeNode(String name) {
        if (!graphNodes.containsKey(name)) {
            return null;
        }
        Node nodeToRemove = graphNodes.get(name);
        graphNodes.remove(name);

        graphNodes.values().stream()
                // Filter nodes that are not the removed node
                .filter(node -> !node.getName().equals(name))
                // Remove edges connected to the removed node
                .forEach(node -> node.removeNeighbour(name));

        return nodeToRemove;
    }

    public Node removeEdge(String origin, String destination) {
        Node originNode = graphNodes.get(origin);
        if (originNode == null) return null;
        return originNode.removeNeighbour(destination) ? originNode : null;
    }

    public int findShortestPath(String origin, String destiny) {

        Node originNode = graphNodes.get(origin);
        if (originNode == null) return -1;
        Node destinyNode = graphNodes.get(destiny);
        if (destinyNode == null) return -1;

        Map<Node, Integer> distanceMap = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distanceMap::get));
        Set<Node> visited = new HashSet<>();

        // Initialize distances from source node
        for (Node node : graphNodes.values()) {
            distanceMap.put(node, node == originNode ? 0 : Integer.MAX_VALUE);
        }

        pq.offer(originNode);

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            if (currentNode == destinyNode) {
                break;
            }
            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);

            for (Edge edge : currentNode.getNeighbours()) {
                Node neighbor = edge.getDestinyNode();
                int newDistance = distanceMap.get(currentNode) + edge.getWeight();
                if (newDistance < distanceMap.get(neighbor)) {
                    distanceMap.put(neighbor, newDistance);
                    pq.offer(neighbor);
                }
            }
        }

        return distanceMap.get(destinyNode);

    }

    public Set<Node> findNodesCloserThanWeight(int weightLimit, String startNodeName) {

        Node startNode = graphNodes.get(startNodeName);
        if (startNode == null) return null;

        Set<Node> closerNodes = new HashSet<>();
        Map<Node, Integer> distanceMap = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();

        for (Node node : graphNodes.values()) {
            distanceMap.put(node, node == startNode ? 0 : Integer.MAX_VALUE);
        }

        queue.offer(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            int currentDistance = distanceMap.get(currentNode);

            for (Edge edge : currentNode.getNeighbours()) {
                Node neighbor = edge.getDestinyNode();
                int newDistance = currentDistance + edge.getWeight();
                if (newDistance >= distanceMap.get(neighbor)) continue;
                distanceMap.put(neighbor, newDistance);
                if (newDistance >= weightLimit) continue;
                closerNodes.add(neighbor);
                queue.offer(neighbor);
            }
        }
        return closerNodes;
    }

}
