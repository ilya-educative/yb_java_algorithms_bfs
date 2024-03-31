package com.example.javafxdemo.service.bfs;

import com.example.javafxdemo.animation.AnimationUtils;
import com.example.javafxdemo.model.Node;
import com.example.javafxdemo.model.NodeType;
import com.example.javafxdemo.view.NodeView;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.TextArea;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class BreadthFirstTraversal {
    public static SequentialTransition execute(Map<Node, NodeView> graph, Node from, TextArea consoleTextArea) {
        clearVisitedNodes(graph.keySet());
        
        SequentialTransition sequentialTransition = new SequentialTransition();

        ParallelTransition parallelTransition = new ParallelTransition();
        graph.values().stream()
                .map(nodeView -> AnimationUtils.fillCircle(nodeView.circle, NodeType.Common))
                .forEach(animation -> parallelTransition.getChildren().add(animation));
        sequentialTransition.getChildren().add(parallelTransition);

        sequentialTransition.getChildren().add(AnimationUtils.fillCircle(graph.get(from).circle, NodeType.From));

        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        int iteration = 1;
        queue.offer(from);
        visited.add(from);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            node.isVisited = true;
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(consoleTextArea, iterationMessage(iteration++)));
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(consoleTextArea, visitedNodeMessage(node)));
            sequentialTransition.getChildren().add(AnimationUtils.fillCircle(graph.get(node).circle, NodeType.Visited));

            for (Node neighbour : node.neighbours) {
                if (!visited.contains(neighbour)) {
                    queue.offer(neighbour);
                    visited.add(neighbour);
                    sequentialTransition.getChildren().add(AnimationUtils.fillCircle(graph.get(neighbour).circle, NodeType.Neighbour));
                }
            }
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(consoleTextArea, neighboursMessage(node, List.of(node.neighbours))));
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(consoleTextArea, unvisitedNodesMessage(queue)));
        }
        return sequentialTransition;
    }

    private static void clearVisitedNodes(Collection<Node> nodes) {
        nodes.forEach(node -> node.isVisited = false);
    }

    private static String iterationMessage(int iteration) {
        return "Iteration: " + iteration;
    }
    private static String visitedNodeMessage(Node node) {
        return String.format("Visiting node '%s'", node.letter);
    }
    private static String neighboursMessage(Node node, Collection<Node> nodes) {
        String neighbours = nodes.stream()
                .map(Node::toString)
                .collect(Collectors.joining("', '", "['", "']"));
        return String.format("Node '%s' neighbours are %s", node.letter, neighbours);
    }
    private static String unvisitedNodesMessage(Queue<Node> queue) {
        String neighbours = queue.stream()
                .map(Node::toString)
                .collect(Collectors.joining("', '", "['", "']"));
        return String.format("Nodes left to visit %s", neighbours);
    }
}
