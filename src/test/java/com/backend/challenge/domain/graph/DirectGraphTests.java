package com.backend.challenge.domain.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DirectGraphTests {

    private static DirectGraph directGraph;

    /**
     * 2 - 5
     * /     \
     * /       8
     * /       / \
     * 1 - 3 - 6  10
     * \       \ /
     * \       9
     * \     /
     * 4 - 7
     */
    @BeforeEach
    public void populateGraph() {
        directGraph = new DirectGraph();
        IntStream.rangeClosed(1, 10).forEach(i -> directGraph.addNode("Node-" + i));
        directGraph.addEdge("Node-1", "Node-2", 1);
        directGraph.addEdge("Node-1", "Node-3", 2);
        directGraph.addEdge("Node-1", "Node-4", 3);
        directGraph.addEdge("Node-2", "Node-5", 1);
        directGraph.addEdge("Node-3", "Node-6", 1);
        directGraph.addEdge("Node-4", "Node-7", 1);
        directGraph.addEdge("Node-5", "Node-8", 1);
        directGraph.addEdge("Node-6", "Node-8", 1);
        directGraph.addEdge("Node-6", "Node-9", 1);
        directGraph.addEdge("Node-7", "Node-9", 1);
        directGraph.addEdge("Node-8", "Node-10", 1);
        directGraph.addEdge("Node-9", "Node-10", 1);
    }

    @Test
    void testAddNodeWithSuccess() {
        // Arrange
        String nodeName = "I-AM-BEER";
        Node expect = Node.builder().name(nodeName).neighbours(new HashSet<>()).build();
        Node result;

        // Act
        result = directGraph.addNode(nodeName);

        // Assert
        assertEquals(expect.getName(), result.getName());
        assertEquals(expect.getNeighbours().size(), result.getNeighbours().size());
    }

    @Test
    void testAddNodeWithAlreadyExistingNode() {
        // Arrange
        String nodeName = "Node-1";
        Node result;

        // Act
        result = directGraph.addNode(nodeName);

        // Assert
        assertNull(result);
    }

    @Test
    void testAddEdgeWithSuccess() {
        String origin = "Node-1";
        String destiny = "Node-10";
        int weight = 1;

        Edge expect = Edge.builder()
                .destinyNode(Node.builder().name(destiny).neighbours(new HashSet<>()).build())
                .weight(weight)
                .build();

        Edge result;

        // Act
        result = directGraph.addEdge(origin, destiny, weight);

        // Assert
        assertEquals(expect.getDestinyNode().getName(), result.getDestinyNode().getName());
        assertEquals(expect.getWeight(), result.getWeight());
    }

    @Test
    void testAddEdgeWithMissingOriginNode() {
        String origin = "Node-XXX";
        String destiny = "Node-2";
        int weight = 1;

        Edge result;

        // Act
        result = directGraph.addEdge(origin, destiny, weight);

        // Assert
        assertNull(result);
    }

    @Test
    void testAddEdgeWithMissingDestinyNode() {
        String origin = "Node-1";
        String destiny = "Node-XXX";
        int weight = 1;

        Edge result;

        // Act
        result = directGraph.addEdge(origin, destiny, weight);

        // Assert
        assertNull(result);
    }

    @Test
    void testRemoveNodeWithSuccess() {
        // Arrange
        String nodeName = "Node-10";
        Node expect = Node.builder().name(nodeName).neighbours(new HashSet<>()).build();
        Node result;

        // Act
        result = directGraph.removeNode(nodeName);

        // Assert
        assertEquals(expect.getName(), result.getName());
        assertEquals(expect.getNeighbours().size(), result.getNeighbours().size());
    }

    @Test
    void testRemoveNodeNotExisting() {
        // Arrange
        String node = "Node-XXX";
        Node result;

        // Act
        result = directGraph.removeNode(node);

        // Assert
        assertNull(result);
    }

    @Test
    void testRemoveEdgeWithSuccess() {
        // Arrange
        String originNode = "Node-8";
        String destinyNode = "Node-10";
        Node expect = Node.builder().name(originNode).neighbours(new HashSet<>()).build();
        Node result;

        // Act
        result = directGraph.removeEdge(originNode, destinyNode);

        // Assert
        assertEquals(expect.getName(), result.getName());
        assertEquals(expect.getNeighbours().size(), result.getNeighbours().size());
    }

    @Test
    void testRemoveEdgeWithNonExistingDestiny() {
        // Arrange
        String originNode = "Node-8";
        String destinyNode = "Node-XXX";
        Node expect = Node.builder().name(originNode).neighbours(new HashSet<>() {{
            add(Edge.builder().build()); // yes, the edge will not be the same, but for this test only the size is important
        }}).build();
        Node result;

        // Act
        result = directGraph.removeEdge(originNode, destinyNode);

        // Assert
        assertEquals(expect.getName(), result.getName());
        assertEquals(expect.getNeighbours().size(), result.getNeighbours().size());
    }

    @Test
    void testRemoveEdgeWithoutExisting() {
        // Arrange
        String originNode = "Node-8";
        String destinyNode = "Node-9";
        Node expect = Node.builder().name(originNode).neighbours(new HashSet<>() {{
            add(Edge.builder().build()); // yes, the edge will not be the same, but for this test only the size is important
        }}).build();
        Node result;

        // Act
        result = directGraph.removeEdge(originNode, destinyNode);

        // Assert
        assertEquals(expect.getName(), result.getName());
        assertEquals(expect.getNeighbours().size(), result.getNeighbours().size());
    }

    @Test
    void testRemoveEdgeWithNonExistingOrigin() {
        // Arrange
        String node = "Node-XXX";
        Node result;

        // Act
        result = directGraph.removeNode(node);

        // Assert
        assertNull(result);
    }

    @Test
    void testFindShortestPathWithSuccess() {
        // Arrange
        String origin = "Node-1";
        String destiny = "Node-10";
        int expResult = 4;
        int result;

        // Act
        result = directGraph.findShortestPath(origin, destiny);

        // Assert
        assertEquals(expResult, result);
    }

    @Test
    void testFindShortestPathWithoutOrigin() {
        // Arrange
        String origin = "Node-XXX";
        String destiny = "Node-10";
        int result;

        // Act
        result = directGraph.findShortestPath(origin, destiny);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    void testFindShortestPathWithoutDestiny() {
        // Arrange
        String origin = "Node-1";
        String destiny = "Node-XXX";
        int result;

        // Act
        result = directGraph.findShortestPath(origin, destiny);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    void testFindShortestPathWithoutPath() {
        // Arrange
        String origin = "Node-1";
        String destiny = "Node-2";
        int expResult = Integer.MAX_VALUE;
        int result;

        // Act
        directGraph.removeEdge(origin, destiny);
        result = directGraph.findShortestPath(origin, destiny);

        // Assert
        assertEquals(expResult, result);
    }

    @Test
    void testFindNodesCloserThanWeightWithSuccess() {
        // Arrange
        String node = "Node-1";
        int weight = 10;
        String result;
        String expResult = "Node-10,Node-2,Node-3,Node-4,Node-5,Node-6,Node-7,Node-8,Node-9";

        // Act
        result = directGraph.findNodesCloserThanWeight(weight, node)
                .stream().map(Node::getName)
                .sorted().collect(Collectors.joining(","));
        // Assert
        assertEquals(expResult, result);
    }

    @Test
    void testFindNodesCloserThanWeightWithMissingOrigin() {
        // Arrange
        String node = "Node-XXX";
        int weight = 0;
        Set<Node> result;

        // Act
        result = directGraph.findNodesCloserThanWeight(weight, node);

        // Assert
        assertNull(result);
    }

    @Test
    void testFindNodesCloserThanWeightWithNoNodesFound() {
        // Arrange
        String node = "Node-1";
        int weight = -1;
        String result;
        String expResult = "";

        // Act
        result = directGraph.findNodesCloserThanWeight(weight, node)
                .stream().map(Node::getName)
                .sorted().collect(Collectors.joining(","));
        // Assert
        assertEquals(expResult, result);
    }

}
