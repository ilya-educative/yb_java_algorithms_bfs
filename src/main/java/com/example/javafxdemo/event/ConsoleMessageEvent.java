package com.example.javafxdemo.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ConsoleMessageEvent extends Event {
    public static final EventType<ConsoleMessageEvent> CONSOLE_MESSAGE_EVENT = new EventType<>(Event.ANY, "CONSOLE_MESSAGE_EVENT");

    private final String message;

    public ConsoleMessageEvent(String message) {
        super(CONSOLE_MESSAGE_EVENT);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
