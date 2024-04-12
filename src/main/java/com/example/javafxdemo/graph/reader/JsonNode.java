package com.example.javafxdemo.graph.reader;

public class JsonNode {
    public final char node;
    public final char[] neighbours;

    public JsonNode(char node, char[] neighbours) {
        this.node = node;
        this.neighbours = neighbours;
    }
}
