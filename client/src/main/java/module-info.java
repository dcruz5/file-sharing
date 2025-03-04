module gh.filesharing.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens gh.filesharing.client to javafx.fxml;
    exports gh.filesharing.client;
}