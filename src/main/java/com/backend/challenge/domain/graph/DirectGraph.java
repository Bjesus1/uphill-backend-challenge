package com.backend.challenge.domain.graph;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * A direct graph representation
 */
@Component
@NoArgsConstructor
public class DirectGraph {

    /**
     * The nodes of the graph.
     */
    Map<String, Node> graphNodes = new HashMap<>();

    /**
     * Adds a node to the graph with the received name.
     *
     * @param name the name of the node.
     * @return the created node if it does not exist in the graph. Otherwise, null.
     */
    public Node addNode(String name) {
        if (graphNodes.containsKey(name)) {
            return null;
        }
        Node node = Node.builder().name(name).neighbours(new HashSet<>()).build();
        graphNodes.putIfAbsent(name, node);
        return node;
    }

    /**
     * Adds a new edge of the graph.
     *
     * @param origin      the origin node name.
     * @param destination the destination node name.
     * @param weight      the weight of the edge.
     * @return the created edge instance. If the received node names' do not exist, returns null.
     */
    public Edge addEdge(String origin, String destination, int weight) {
        Node originNode = graphNodes.get(origin);
        Node destinationNode = graphNodes.get(destination);
        if (originNode == null || destinationNode == null) {
            return null;
        }
        Edge edge = Edge.builder().destinyNode(destinationNode).weight(weight).build();
        return originNode.addNeighbour(edge) ? edge : null;
    }

    /**
     * Removes a node with the given name.
     *
     * @param name the name of the node to be removed.
     * @return The removed node. If it does not exist, returns null.
     */
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

    /**
     * Removes an edge of the graph between the two received nodes.
     *
     * @param origin      the origin node name.
     * @param destination the destination node name.
     * @return the origin node without the edge expected to be removed.
     */
    public Node removeEdge(String origin, String destination) {
        Node originNode = graphNodes.get(origin);
        if (originNode == null) return null;
        return originNode.removeNeighbour(destination) ? originNode : null;
    }

    /**
     * Finds the shortest path (path with the minor weight) between two nodes.
     *
     * @param origin  the origin node name.
     * @param destiny the destiny node name.
     * @return the path between the two received nodes with the lesser weight. However, if the received node names' do
     * not exist, returns null.
     */
    public int findShortestPath(String origin, String destiny) {

        Node originNode = graphNodes.get(origin);
        if (originNode == null) return -1;
        Node destinyNode = graphNodes.get(destiny);
        if (destinyNode == null) return -1;

        Map<Node, Integer> distanceMap = initMap();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distanceMap::get));
        Set<Node> visited = new HashSet<>();

        distanceMap.put(originNode, 0);
        pq.offer(originNode);

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            if (currentNode.equals(destinyNode)) break;
            if (visited.contains(currentNode)) continue;
            visited.add(currentNode);

            currentNode.getNeighbours().forEach(edge -> {
                Node neighbor = edge.getDestinyNode();
                int newDistance = distanceMap.get(currentNode) + edge.getWeight();
                if (newDistance < distanceMap.get(neighbor)) {
                    distanceMap.put(neighbor, newDistance);
                    pq.offer(neighbor);
                }
            });
        }

        return distanceMap.get(destinyNode);

    }

    /**
     * Returns a set of nodes who are closer than to the received node considering the received weight as well.
     *
     * @param weightLimit   the weight restriction.
     * @param startNodeName the reference node for the closest search.
     * @return a set of nodes closer to the received node with an inferior weight to the received weight limit.
     * If the node does not exist, returns null.
     */
    public Set<Node> findNodesCloserThanWeight(int weightLimit, String startNodeName) {

        Node startNode = graphNodes.get(startNodeName);
        if (startNode == null) return null;

        Set<Node> closerNodes = new HashSet<>();
        Map<Node, Integer> distanceMap = initMap();
        Queue<Node> queue = new LinkedList<>();

        distanceMap.put(startNode, 0);
        queue.offer(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            int currentDistance = distanceMap.get(currentNode);

            currentNode.getNeighbours().forEach(edge -> {
                Node neighbor = edge.getDestinyNode();
                int newDistance = currentDistance + edge.getWeight();
                if (newDistance >= distanceMap.get(neighbor)) return;
                distanceMap.put(neighbor, newDistance);
                if (newDistance >= weightLimit) return;
                closerNodes.add(neighbor);
                queue.offer(neighbor);
            });
        }
        return closerNodes;
    }

    /**
     * Initiates a map of nodes with the Integer.MAX_VALUE.
     *
     * @return an HashMap of nodes with the Integer.MAX_VALUE as their value.
     */
    private Map<Node, Integer> initMap() {
        Map<Node, Integer> mapToInit = new HashMap<>();
        for (Node node : graphNodes.values()) {
            mapToInit.put(node, Integer.MAX_VALUE);
        }
        return mapToInit;
    }

}
