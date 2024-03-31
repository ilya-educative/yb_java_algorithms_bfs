package com.example.javafxdemo.view;

import com.example.javafxdemo.model.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class GraphViewUtils {
    public static void populateGraph(Map<Node, NodeView> graph, Node[] nodes, Pane graphPlaygroundPane) {
        for (Node node : nodes) {
            graph.put(node, new NodeView(node));
        }
        graph.values().forEach(nodeView -> graphPlaygroundPane.getChildren().add(nodeView));
    }

    public static void clearGraph(Map<Node, NodeView> graph, Pane graphPlaygroundPane) {
        graphPlaygroundPane.getChildren().clear();
        graph.clear();
    }

    public static void setLayoutForGraph(Map<Node, NodeView> graph, Pane graphPlaygroundPane) {

    }

    public static Node setNodeFromNodeA(Map<Node, NodeView> graph) {
        return graph.keySet().stream()
                .filter(node -> node.letter == 'a' || node.letter == 'A')
                .findFirst()
                .orElseThrow();
    }

    public static void drawEdgesBetweenNodes(Map<Node, NodeView> graph, Node from, Pane graphPlaygroundPane) {
        Queue<Node> queue = new LinkedList<>();
        queue.offer(from);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            node.isVisited = true;

            for (Node neighbour : node.neighbours) {
                if (!neighbour.isVisited) {
                    queue.offer(neighbour);
                    createLineBetweenCircles(graphPlaygroundPane, graph.get(node), graph.get(neighbour));
                }
            }
        }
    }

    private static void createLineBetweenCircles(Pane pane, NodeView source, NodeView target) {
        Line line = new Line();
        line.setStroke(Color.BLACK);
        line.startXProperty().bind(source.layoutXProperty());
        line.startYProperty().bind(source.layoutYProperty());
        line.endXProperty().bind(target.layoutXProperty());
        line.endYProperty().bind(target.layoutYProperty());
        pane.getChildren().addFirst(line);
    }
}
