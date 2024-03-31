module com.example.javafxdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports com.example.javafxdemo;
    exports com.example.javafxdemo.controller;

    opens com.example.javafxdemo to javafx.fxml;
    opens com.example.javafxdemo.controller to javafx.fxml;
    opens com.example.javafxdemo.service.graph to com.google.gson;
}