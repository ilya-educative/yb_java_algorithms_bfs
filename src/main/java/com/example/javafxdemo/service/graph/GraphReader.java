package com.example.javafxdemo.service.graph;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class GraphReader {
    public JsonGraph readFromJson(String filename) {
        Gson gson = new Gson();
        File file = new File(filename);

        Exception exception;
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, JsonGraph.class);
        } catch (JsonSyntaxException e) {
            System.out.println("Wrong json syntax");//todo: EventManager.fireEvent(new ConsoleMessageEvent("wrong json syntax"));
            exception = e;
        } catch (IOException e) {
            System.out.println("Can't read file");//todo: EventManager.fireEvent(new ConsoleMessageEvent("can't read file"));
            exception = e;
        }
        throw new RuntimeException(exception);
    }
}
