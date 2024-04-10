package com.backend.challenge.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
public class Node {

    private String name;

    private Set<Edge> neighbours;

    public boolean addNeighbour(Edge edge){
        return neighbours.add(edge);
    }

    public boolean removeNeighbour(String nodeToRemoveName) {
        if (neighbours.stream().map(edge -> edge.getDestinyNode().getName()).noneMatch(nodeToRemoveName::equals)) {
            return true; // does not exist, nothing to do.
        }

        return neighbours.removeIf(edge -> edge.getDestinyNode().getName().equals(nodeToRemoveName));
    }

}
