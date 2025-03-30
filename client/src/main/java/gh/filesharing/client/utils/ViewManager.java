package gh.filesharing.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class ViewManager {
    private final Stage stage;

    public ViewManager(Stage stage) {
        this.stage = stage;
    }

    public void switchView(String fxmlFile, String title) {
        String basePath = "/gh/filesharing/client/view/";
        String path = Paths.get(basePath, fxmlFile).toString();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("Error loading view " + path + ": " + e.getMessage());
            e.printStackTrace();
            AlertManager.showError("Error loading view: " + e.getMessage());
        }
    }
}