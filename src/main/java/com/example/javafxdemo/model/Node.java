package com.example.javafxdemo.model;

import java.util.Objects;

public class Node {
    public final char letter;
    public final Node[] neighbours;

    public boolean isVisited = false;

    public Node(char letter) {
        this.letter = letter;
        neighbours = new Node[0];
    }

    public Node(char letter, Node[] neighbours) {
        this.letter = letter;
        this.neighbours = neighbours;
    }

    @Override public String toString() {
        return String.valueOf(letter);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return letter == node.letter;
    }

    @Override public int hashCode() {
        return Objects.hash(letter);
    }
}
