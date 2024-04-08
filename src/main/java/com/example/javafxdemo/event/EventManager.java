package com.example.javafxdemo.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.HashMap;
import java.util.Map;

public class EventManager {
    private static final Map<EventType<?>, EventHandler<?>> eventHandlers = new HashMap<>();

    public static <T extends Event> void addListener(EventType<T> eventType, EventHandler<? super T> handler) {
        eventHandlers.put(eventType, handler);
    }

    @SuppressWarnings("unchecked")
    public static void fireEvent(Event event) {
        EventHandler<? super Event> handler = (EventHandler<? super Event>) eventHandlers.get(event.getEventType());
        if (handler != null) {
            handler.handle(event);
        }
    }
}
