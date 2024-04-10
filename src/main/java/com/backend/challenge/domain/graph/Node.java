package com.backend.challenge.domain.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * Node representation.
 */
@Builder
@AllArgsConstructor
@Getter
public class Node {

    /**
     * The name of the node.
     */
    private String name;

    /**
     * A set of the node edges.
     */
    private Set<Edge> neighbours;

    /**
     * Adds a new edge to the neighbours set.
     *
     * @param edge the new edge to add.
     * @return true if the edge is added or is already present. Otherwise, false.
     */
    public boolean addNeighbour(Edge edge) {
        return neighbours.add(edge);
    }

    /**
     * Removes the edges where the destination corresponds to the received node name.
     *
     * @param nodeToRemoveName the destination node name.
     * @return true if an edge with the destination node does not exist or it is removed. Otherwise, false.
     */
    public boolean removeNeighbour(String nodeToRemoveName) {
        if (neighbours.stream().map(edge -> edge.getDestinyNode().getName()).noneMatch(nodeToRemoveName::equals)) {
            return true; // does not exist, nothing to do.
        }

        return neighbours.removeIf(edge -> edge.getDestinyNode().getName().equals(nodeToRemoveName));
    }

}
