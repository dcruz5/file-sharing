module gh.filesharing.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.formdev.flatlaf;
    requires java.desktop;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires com.fasterxml.jackson.databind;
    requires bcrypt;

    exports gh.filesharing.client;
    exports gh.filesharing.client.controllers;

    opens gh.filesharing.client to javafx.fxml;
    opens gh.filesharing.client.controllers to javafx.fxml;
    opens gh.filesharing.client.utils to com.fasterxml.jackson.databind;
}