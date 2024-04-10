package com.backend.challenge.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Edge {

    private Node destinyNode;

    private int weight;

}
