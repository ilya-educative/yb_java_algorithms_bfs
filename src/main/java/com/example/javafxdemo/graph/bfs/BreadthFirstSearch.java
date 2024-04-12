package com.example.javafxdemo.graph.bfs;

import com.example.javafxdemo.animation.AnimationUtils;
import com.example.javafxdemo.model.Node;
import com.example.javafxdemo.model.NodeType;
import com.example.javafxdemo.graph.view.GraphViewUtils;
import com.example.javafxdemo.graph.view.NodeView;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class BreadthFirstSearch {
    public static SequentialTransition execute(Map<Node, NodeView> graph, boolean findPath, boolean useBlockedNodes) {
        clearVisitedNodes(graph.keySet());

        SequentialTransition sequentialTransition = new SequentialTransition();

        ParallelTransition parallelTransition = new ParallelTransition();
        graph.values().stream()
                .map(nodeView -> AnimationUtils.fillCircle(nodeView.circle, NodeType.Common))
                .forEach(animation -> parallelTransition.getChildren().add(animation));
        sequentialTransition.getChildren().add(parallelTransition);

        Node from;
        Node to = null;
        boolean pathFound = false;
        if (findPath) {
            from = GraphViewUtils.getFromNode(graph).orElseThrow();
            to = GraphViewUtils.getToNode(graph).orElseThrow();
        } else {
            from = GraphViewUtils.getFromAnyNode(graph);
        }
        sequentialTransition.getChildren().add(AnimationUtils.fillCircle(graph.get(from).circle, NodeType.From));

        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        int iteration = 1;
        queue.offer(from);
        visited.add(from);

        OuterWhile:
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            node.isVisited = true;
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(iterationMessage(iteration++)));
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(visitedNodeMessage(node)));
            sequentialTransition.getChildren().add(AnimationUtils.fillCircle(graph.get(node).circle, NodeType.Visited));

            for (Node neighbour : node.neighbours) {
                if (useBlockedNodes && neighbour.type == NodeType.Blocked) {
                    sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(blockedNodeMessage(neighbour)));
                    sequentialTransition.getChildren().add(AnimationUtils.fillCircle(graph.get(neighbour).circle, NodeType.Blocked));
                    continue;
                }

                if (!visited.contains(neighbour)) {
                    queue.offer(neighbour);
                    visited.add(neighbour);
                    neighbour.connectedTo = node;
                    sequentialTransition.getChildren().add(AnimationUtils.fillCircle(graph.get(neighbour).circle, NodeType.Neighbour));
                }

                if (findPath && neighbour.equals(to)) {
                    pathFound = true;
                    List<Node> path = buildPath(neighbour, graph, sequentialTransition);
                    sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(finalPathMessage(path)));
                    break OuterWhile;
                }
            }
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(neighboursMessage(node, List.of(node.neighbours))));
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(unvisitedNodesMessage(queue)));
        }
        if (findPath && !pathFound) {
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(pathNotFoundMessage(from, to)));
        }
        return sequentialTransition;
    }

    private static void clearVisitedNodes(Collection<Node> nodes) {
        nodes.forEach(node -> node.isVisited = false);
    }

    private static List<Node> buildPath(Node node, Map<Node, NodeView> graph, SequentialTransition sequentialTransition) {
        List<Node> path = new ArrayList<>();

        while (node != null) {
            path.add(node);
            sequentialTransition.getChildren().add(AnimationUtils.fillCircle(graph.get(node).circle, NodeType.Path));
            sequentialTransition.getChildren().add(AnimationUtils.graduallySetText(addNodeToPathMessage(node, path)));
            node = node.connectedTo;
        }

        return path.reversed();
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
    private static String addNodeToPathMessage(Node node, Collection<Node> path) {
        String tmpPath = path.stream()
                .map(Node::toString)
                .collect(Collectors.joining("', '", "['", "']"));
        return String.format("Node '%s' added to path %s", node.letter, tmpPath);
    }
    private static String finalPathMessage(Collection<Node> path) {
        String finalPath = path.stream()
                .map(Node::toString)
                .collect(Collectors.joining("', '", "['", "']"));
        return String.format("Final path %s", finalPath);
    }
    private static String blockedNodeMessage(Node node) {
        return String.format("Node '%s' can't be visited because it is blocked", node);
    }
    private static String pathNotFoundMessage(Node from, Node to) {
        return String.format("Path from '%s' to '%s' not found", from, to);
    }
}
