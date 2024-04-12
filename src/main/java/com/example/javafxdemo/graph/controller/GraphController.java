package com.example.javafxdemo.graph.controller;

import com.example.javafxdemo.model.Node;
import com.example.javafxdemo.graph.bfs.BreadthFirstSearch;
import com.example.javafxdemo.graph.GraphService;
import com.example.javafxdemo.graph.view.GraphViewUtils;
import com.example.javafxdemo.graph.view.NodeView;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GraphController {
    private final GraphService graphService;

    private final Map<Node, NodeView> graph = new HashMap<>();

    /* Background */
    @FXML private ScrollPane playgroundScrollPane;
    @FXML private Pane playgroundPane;
    /* User Input */
    @FXML private TextField blockedNodesTextArea;
    @FXML private TextField fromNodeTextArea;
    @FXML private TextField toNodeTextArea;
    /* Controls */
    @FXML private CheckBox useBlockedNodesCheckBox;
    @FXML private CheckBox findPathCheckBox;
    @FXML private TextArea fileTextArea;
    @FXML private Button clearNodesButton;
    @FXML private Button bfsButton;

    public GraphController() {
        graphService = new GraphService();
    }

    public void initialize() {
        addZooming();
        addOnFileUpload();
        bfsButton.setDisable(true);
    }

    private void addZooming() {
        playgroundScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double deltaY = event.getDeltaY();
                double scale = deltaY > 0 ? 1.1 : 0.9;
                playgroundPane.setScaleX(playgroundPane.getScaleX() * scale);
                playgroundPane.setScaleY(playgroundPane.getScaleY() * scale);
                event.consume();
            }
        });
    }
    private void addOnFileUpload() {
        fileTextArea.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                handleFileUpload(selectedFile.getAbsolutePath());
            }
        });
        fileTextArea.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        fileTextArea.setOnDragDropped(event -> {
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
        GraphViewUtils.clearGraph(graph, playgroundPane);
        GraphViewUtils.populateGraph(graph, nodes, playgroundPane);
        GraphViewUtils.drawEdgesBetweenNodes(graph, GraphViewUtils.getFromAnyNode(graph), playgroundPane);
        GraphViewUtils.graphAddNodeViewOnClickListener(graph, fromNodeTextArea, toNodeTextArea, blockedNodesTextArea);
        GraphViewUtils.setLayoutForGraph(graph, playgroundPane);
        bfsButton.setDisable(false);
    }

    public void onBFSButtonClick() {
        bfsButton.setDisable(true);
        clearNodesButton.setDisable(true);
        SequentialTransition sequentialTransition = BreadthFirstSearch.execute(graph, findPathCheckBox.isSelected(), useBlockedNodesCheckBox.isSelected());
        sequentialTransition.getChildren().forEach(animation -> animation.setDelay(Duration.millis(300)));
        sequentialTransition.setOnFinished(event -> {
            bfsButton.setDisable(false);
            clearNodesButton.setDisable(false);
        });
        sequentialTransition.play();
    }
    public void onClearNodesButtonClick() {
        GraphViewUtils.clearNodes(graph, fromNodeTextArea, toNodeTextArea, blockedNodesTextArea, findPathCheckBox, useBlockedNodesCheckBox);
    }
}
