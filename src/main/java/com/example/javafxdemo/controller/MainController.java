package com.example.javafxdemo.controller;

import com.example.javafxdemo.model.Node;
import com.example.javafxdemo.service.bfs.BreadthFirstTraversal;
import com.example.javafxdemo.service.graph.GraphService;
import com.example.javafxdemo.view.GraphViewUtils;
import com.example.javafxdemo.view.NodeView;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Dragboard;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    /* Utils */
    @FXML private TextArea consoleTextArea;

    public MainController() {
        graphService = new GraphService();
    }

    /* Graph */
    private final GraphService graphService;
    private final Map<Node, NodeView> graph = new HashMap<>();
    private SequentialTransition graphSequentialTransition;
    private boolean onGraphBFSButtonPause = true;

    @FXML private ScrollPane graphPlaygroundScrollPane;
    @FXML private TextArea graphDragAndDropTextArea;
    @FXML private Pane graphPlaygroundPane;
    @FXML private Button graphBFSButton;

    private void addZooming() {
        graphPlaygroundScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double deltaY = event.getDeltaY();
                double scale = deltaY > 0 ? 1.1 : 0.9;
                graphPlaygroundPane.setScaleX(graphPlaygroundPane.getScaleX() * scale);
                graphPlaygroundPane.setScaleY(graphPlaygroundPane.getScaleY() * scale);
                event.consume();
            }
        });
    }
    private void addOnFileUpload() {
        graphDragAndDropTextArea.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                handleFileUpload(selectedFile.getAbsolutePath());
            }
        });
        graphDragAndDropTextArea.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        graphDragAndDropTextArea.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                handleFileUpload(dragboard.getFiles().get(0).getAbsolutePath());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
    private void handleFileUpload(String filename) {
        Node[] nodes = graphService.readNodes(filename);
        GraphViewUtils.clearGraph(graph, graphPlaygroundPane);
        GraphViewUtils.populateGraph(graph, nodes, graphPlaygroundPane);
        GraphViewUtils.drawEdgesBetweenNodes(graph, GraphViewUtils.setNodeFromNodeA(graph), graphPlaygroundPane);
        graphSequentialTransition = BreadthFirstTraversal.execute(graph, GraphViewUtils.setNodeFromNodeA(graph), consoleTextArea);
        graphSequentialTransition.getChildren().forEach(animation -> animation.setDelay(Duration.millis(300)));
        graphSequentialTransition.setOnFinished(event -> graphBFSButton.setText("Play"));
        setGraphBFSButtonState();
    }
    private void setGraphBFSButtonState() {
        graphBFSButton.setDisable(graphSequentialTransition == null);
    }

    public void onGraphBFSPlayButtonAction() {
        if (onGraphBFSButtonPause) {
            graphBFSButton.setText("Pause");
            onGraphBFSButtonPause = false;
            graphSequentialTransition.play();
        } else {
            graphBFSButton.setText("Play");
            onGraphBFSButtonPause = true;
            graphSequentialTransition.pause();
        }
    }

    /* Matrix */



    public void initialize() {
        addZooming();
        addOnFileUpload();
        setGraphBFSButtonState();
    }
}
