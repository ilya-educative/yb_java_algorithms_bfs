package com.example.javafxdemo.animation;

import com.example.javafxdemo.event.EventManager;
import com.example.javafxdemo.event.ConsoleMessageEvent;
import com.example.javafxdemo.model.NodeType;
import com.example.javafxdemo.view.GraphViewUtils;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class AnimationUtils {
    public static Animation graduallySetText(String text) {
        return new Transition() {
            {
                setCycleDuration(Duration.ONE);
                setOnFinished(event -> EventManager.fireEvent(new ConsoleMessageEvent(text + "\n")));
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
        fillTransition.setToValue(GraphViewUtils.getCircleColor(nodeType));
        return fillTransition;
    }
}
