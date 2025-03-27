module gh.filesharing.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.formdev.flatlaf;
    requires java.desktop;
    requires bcrypt;
    requires java.net.http;
    requires jdk.httpserver;


    opens gh.filesharing.client to javafx.fxml;
    exports gh.filesharing.client;
    exports gh.filesharing.client.controllers;
    opens gh.filesharing.client.controllers to javafx.fxml;
}