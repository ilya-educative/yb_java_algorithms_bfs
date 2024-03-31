package com.example.javafxdemo.view;

import com.example.javafxdemo.model.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class NodeView extends Pane {
    public final Circle circle;
    public final Node node;

    public NodeView(Node node) {
        this.node = node;
        this.circle = createCircle();
        addTextInCircle();
        addOnMouseListeners();
        setLayout();
    }

    private Circle createCircle() {
        Circle circle = new Circle();
        circle.setRadius(30.0);
        circle.setFill(Color.web("#34495E"));
        getChildren().add(circle);
        return circle;
    }

    private void addTextInCircle() {
        Text text = new Text(String.valueOf(node.letter));
        text.setFill(Color.WHITE);
        text.translateXProperty().bind(circle.centerXProperty().subtract(text.layoutBoundsProperty().getValue().getWidth() / 2));
        text.translateYProperty().bind(circle.centerYProperty().add(text.layoutBoundsProperty().getValue().getHeight() / 4));
        getChildren().add(text);
    }

    private double mouseX, mouseY;

    private void addOnMouseListeners() {
        setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });
        setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;
            setLayoutX(getLayoutX() + deltaX);
            setLayoutY(getLayoutY() + deltaY);
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });
    }

    //todo: Move setLayout method to GraphViewUtils.setLayoutForGraph();
    private static double offset = 40.0;

    private void setLayout() {
        setLayoutX(offset);
        setLayoutY(offset);
        offset += 40.0;
    }
}
