package com.example.javafxdemo.animation;

import com.example.javafxdemo.model.NodeType;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class AnimationUtils {
    public static Animation graduallySetText(TextArea textArea, String text) {
        return new Transition() {
            {
                setCycleDuration(Duration.ONE);
                setOnFinished(event -> textArea.appendText(text + "\n"));
            }

            @Override protected void interpolate(double frac) {
//                int n = Math.round(text.length() * (float) frac);
//                textArea.insertText(textArea.getLength(), "");
//                textArea.insertText(textArea.getLength(), text.substring(0, n));
            }
        };
    }

    public static Animation fillCircle(Shape shape, NodeType nodeType) {
        FillTransition fillTransition = new FillTransition();
        fillTransition.setShape(shape);
        fillTransition.setToValue(Color.web(getCircleColor(nodeType)));
        return fillTransition;
    }

    private static String getCircleColor(NodeType nodeType) {
        return switch (nodeType) {
            case Common -> "#34495E";
            case From -> "#F39C12";
            case Neighbour -> "#3498DB";
            case Visited -> "#9B59B6";
        };
    }
}
