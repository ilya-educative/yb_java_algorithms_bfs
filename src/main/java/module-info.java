module com.example.javafxdemo {
    requires java.desktop;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.google.gson;

    exports com.example.javafxdemo;
    exports com.example.javafxdemo.graph.controller;
    exports com.example.javafxdemo.handler;

    opens com.example.javafxdemo to javafx.fxml;
    opens com.example.javafxdemo.handler to javafx.fxml;
    opens com.example.javafxdemo.graph.controller to javafx.fxml;
    opens com.example.javafxdemo.graph to com.google.gson;
    opens com.example.javafxdemo.graph.mapper to com.google.gson;
    opens com.example.javafxdemo.graph.reader to com.google.gson;
}