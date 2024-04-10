package com.backend.challenge.domain.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Edge representation.
 */
@Builder
@AllArgsConstructor
@Getter
public class Edge {

    /**
     * The destiny node of the edge.
     */
    private Node destinyNode;

    /**
     * The weight of the edge.
     */
    private int weight;

}
