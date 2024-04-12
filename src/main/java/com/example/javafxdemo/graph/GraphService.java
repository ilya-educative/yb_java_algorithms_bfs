package com.example.javafxdemo.graph;

import com.example.javafxdemo.model.Node;
import com.example.javafxdemo.graph.mapper.GraphMapper;
import com.example.javafxdemo.graph.reader.GraphReader;
import com.example.javafxdemo.graph.reader.JsonGraph;

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
