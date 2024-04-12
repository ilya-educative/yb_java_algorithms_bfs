package com.example.javafxdemo.view;

import com.example.javafxdemo.model.Node;
import com.example.javafxdemo.model.NodeType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

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

    public static void drawEdgesBetweenNodes(Map<Node, NodeView> graph, Node from, Pane graphPlaygroundPane) {
        // fixme: draw too many lines because of isVisited variable
        //  (Use Set<Node> visitedNodes instead)
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
    public static void setLayoutForGraph(Map<Node, NodeView> graph, Pane graphPlaygroundPane) {
        double coolingFactor = 0.95;
        double repulsionFactor = 0.1;
        int iterations = 1000;
        double width = graphPlaygroundPane.getWidth();
        double height = graphPlaygroundPane.getHeight();

        Random random = new Random();
        for (NodeView nodeView : graph.values()) {
            nodeView.setLayoutX(random.nextDouble() * width);
            nodeView.setLayoutY(random.nextDouble() * height);
        }

        for (int i = 0; i < iterations; i++) {
            for (NodeView nodeView : graph.values()) {
                double forceX = 0;
                double forceY = 0;
                for (NodeView otherNodeView : graph.values()) {
                    if (nodeView != otherNodeView) {
                        double dx = otherNodeView.getLayoutX() - nodeView.getLayoutX();
                        double dy = otherNodeView.getLayoutY() - nodeView.getLayoutY();
                        double distance = Math.sqrt(dx * dx + dy * dy);

                        forceX -= repulsionFactor * dx / (distance * distance);
                        forceY -= repulsionFactor * dy / (distance * distance);
                    }
                }
                nodeView.setLayoutX(nodeView.getLayoutX() + forceX);
                nodeView.setLayoutY(nodeView.getLayoutY() + forceY);
            }
            repulsionFactor *= coolingFactor;
        }
    }

    public static void graphAddNodeViewOnClickListener(Map<Node, NodeView> graph, TextField graphFromNodeTextArea, TextField graphToNodeTextArea, TextField graphBlockedNodesTextArea) {
        graph.values().forEach(nodeView -> nodeView.setOnMousePressed(event -> {
            if (event.isControlDown() && event.isPrimaryButtonDown()) {
                System.out.println("Ctrl + Left click");
                GraphViewUtils.resetPreviousFromNode(graph);
                nodeView.node.type = NodeType.From;
                nodeView.circle.setFill(GraphViewUtils.getCircleColor(NodeType.From));
                graphFromNodeTextArea.setText(nodeView.node.toString());
            } else if (event.isControlDown() && event.isSecondaryButtonDown()) {
                System.out.println("Ctrl + Right click");
                GraphViewUtils.resetPreviousToNode(graph);
                nodeView.node.type = NodeType.To;
                nodeView.circle.setFill(GraphViewUtils.getCircleColor(NodeType.To));
                graphToNodeTextArea.setText(nodeView.node.toString());
            } else if (event.isShiftDown() && event.isPrimaryButtonDown()){
                System.out.println("Shift + Left click");
                if (nodeView.node.type == NodeType.Blocked) {
                    nodeView.node.type = NodeType.Common;
                    nodeView.circle.setFill(GraphViewUtils.getCircleColor(NodeType.Common));
                } else {
                    nodeView.node.type = NodeType.Blocked;
                    nodeView.circle.setFill(GraphViewUtils.getCircleColor(NodeType.Blocked));
                }
                String blockedNodes = graph.keySet().stream()
                        .filter(node -> NodeType.Blocked == node.type)
                        .map(Node::toString)
                        .collect(Collectors.joining(","));
                graphBlockedNodesTextArea.setText(blockedNodes);
            }
            nodeView.mouseX = event.getSceneX();
            nodeView.mouseY = event.getSceneY();
        }));
        graph.values().forEach(nodeView -> nodeView.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - nodeView.mouseX;
            double deltaY = event.getSceneY() - nodeView.mouseY;
            nodeView.setLayoutX(nodeView.getLayoutX() + deltaX);
            nodeView.setLayoutY(nodeView.getLayoutY() + deltaY);
            nodeView.mouseX = event.getSceneX();
            nodeView.mouseY = event.getSceneY();
        }));
    }
    private static void resetPreviousToNode(Map<Node, NodeView> graph) {
        GraphViewUtils.getToNode(graph).ifPresent(node -> {
            NodeView toNodeView = graph.get(node);
            node.type = NodeType.Common;
            toNodeView.circle.setFill(GraphViewUtils.getCircleColor(node.type));
        });
    }
    private static void resetPreviousFromNode(Map<Node, NodeView> graph) {
        GraphViewUtils.getFromNode(graph).ifPresent(node -> {
            NodeView toNodeView = graph.get(node);
            node.type = NodeType.Common;
            toNodeView.circle.setFill(GraphViewUtils.getCircleColor(node.type));
        });
    }

    public static Node getFromAnyNode(Map<Node, NodeView> graph) {
        return graph.keySet().stream().findFirst().orElseThrow();
    }
    public static Optional<Node> getFromNode(Map<Node, NodeView> graph) {
        return getNodeByType(graph, NodeType.From);
    }
    public static Optional<Node> getToNode(Map<Node, NodeView> graph) {
        return getNodeByType(graph, NodeType.To);
    }
    private static Optional<Node> getNodeByType(Map<Node, NodeView> graph, NodeType type) {
        return graph.keySet().stream()
                .filter(node -> node.type == type)
                .findFirst();
    }

    public static Color getCircleColor(NodeType nodeType) {
        return Color.web(switch (nodeType) {
            case From -> "#F39C12";
            case To -> "#E74C3C";
            case Common -> "#34495E";
            case Neighbour -> "#3498DB";
            case Visited -> "#9B59B6";
            case Blocked -> "#595F66";
            case Path -> "#27AE60";
        });
    }
}
