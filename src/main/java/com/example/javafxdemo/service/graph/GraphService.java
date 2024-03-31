package com.example.javafxdemo.service.graph;

import com.example.javafxdemo.model.Node;

public class GraphService {
    private final GraphReader graphReader;
    private final GraphMapper graphMapper;

    public GraphService() {
        graphReader = new GraphReader();
        graphMapper = new GraphMapper();
    }

    public Node[] readNodes(String filename) {
        JsonGraph jsonGraph = graphReader.readFromJson(filename);
        return graphMapper.map(jsonGraph.nodes);
    }
}
