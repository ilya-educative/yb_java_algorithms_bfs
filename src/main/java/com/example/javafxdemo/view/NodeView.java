package com.example.javafxdemo.view;

import com.example.javafxdemo.model.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class NodeView extends Pane {
    public final Circle circle;
    public final Node node;
    public double mouseX, mouseY;

    public NodeView(Node node) {
        this.node = node;
        this.circle = createCircle();
        addTextInCircle();
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

    //todo: Move setLayout method to GraphViewUtils.setLayoutForGraph();
    private static double offset = 40.0;

    private void setLayout() {
        setLayoutX(offset);
        setLayoutY(offset);
        offset += 40.0;
    }
}
