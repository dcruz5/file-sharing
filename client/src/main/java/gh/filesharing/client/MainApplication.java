package gh.filesharing.client;

import gh.filesharing.client.utils.ViewManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private ViewManager viewManager;
    private static Stage _stage;

    @Override
    public void start(Stage stage) throws IOException {
        _stage = stage;
        viewManager = new ViewManager(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view/login-register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("File Sharing App");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getStage() {
        return _stage;
    }

    public static void main(String[] args) {
        launch();
    }
}