package com.example.javafxdemo.graph.mapper;

import com.example.javafxdemo.model.Node;
import com.example.javafxdemo.graph.reader.JsonNode;

public class GraphMapper {
    public Node[] map(JsonNode[] jsonNodes) {
        Node[] nodes = new Node[jsonNodes.length];
        for (int i = 0; i < jsonNodes.length; i++) {
            JsonNode jsonNode = jsonNodes[i];
            Node node;
            if (jsonNode.neighbours != null) {
                Node[] neighbours = new Node[jsonNode.neighbours.length];
                for (int j = 0; j < jsonNode.neighbours.length; j++) {
                    neighbours[j] = new Node(jsonNode.neighbours[j]);
                }
                node = new Node(jsonNode.node, neighbours);
            } else {
                node = new Node(jsonNode.node);
            }
            nodes[i] = node;
        }
        arrangeNodes(nodes);
        return nodes;
    }

    private void arrangeNodes(Node[] nodes) {
        for (Node node : nodes) {
            for (int j = 0; j < node.neighbours.length; j++) {
                for (Node value : nodes) {
                    if (value.equals(node.neighbours[j])) {
                        node.neighbours[j] = value;
                    }
                }
            }
        }
    }
}
