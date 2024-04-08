package com.example.javafxdemo.handler;

import com.example.javafxdemo.event.EventManager;
import com.example.javafxdemo.event.ConsoleMessageEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ConsoleMessageHandler {
    @FXML private TextArea consoleTextArea;

    public void initialize() {
        EventManager.addListener(ConsoleMessageEvent.CONSOLE_MESSAGE_EVENT, event -> consoleTextArea.appendText(event.getMessage()));
    }
}
