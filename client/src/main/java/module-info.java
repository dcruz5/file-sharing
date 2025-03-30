module gh.filesharing.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.formdev.flatlaf;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires java.prefs;
    requires java.net.http;
    requires jdk.httpserver;
    requires jjwt.api;

    exports gh.filesharing.client;
    exports gh.filesharing.client.controllers;

    opens gh.filesharing.client to javafx.fxml;
    opens gh.filesharing.client.controllers to javafx.fxml;
    opens gh.filesharing.client.utils to com.fasterxml.jackson.databind;
    opens gh.filesharing.client.classes to com.fasterxml.jackson.databind;
}