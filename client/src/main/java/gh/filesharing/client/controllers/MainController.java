package gh.filesharing.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gh.filesharing.client.utils.AlertManager;

public class MainController {

    public void handleProfileAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profile-view.fxml"));
            Parent profileView = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(profileView));
            stage.setTitle("Mi Perfil");
            stage.show();
        } catch (Exception e) {
            AlertManager.showError("Error al abrir el perfil: " + e.getMessage());
        }
    }

    public void handleLogoutAction(ActionEvent actionEvent) {
        try {
            // Perform logout logic here (e.g., clear session, tokens, etc.)
            AlertManager.showInfo("Sesión cerrada correctamente.");
            // Redirect to login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login-view.fxml"));
            Parent loginView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginView));
            stage.show();
        } catch (Exception e) {
            AlertManager.showError("Error al cerrar sesión: " + e.getMessage());
        }
    }

    public void handleNewUploadAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/upload-view.fxml"));
            Parent uploadView = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(uploadView));
            stage.setTitle("Subir Nuevo Archivo");
            stage.show();
        } catch (Exception e) {
            AlertManager.showError("Error al abrir la ventana de subida: " + e.getMessage());
        }
    }

    public void handleDownloadFileAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/download-view.fxml"));
            Parent downloadView = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(downloadView));
            stage.setTitle("Descargar Archivo");
            stage.show();
        } catch (Exception e) {
            AlertManager.showError("Error al abrir la ventana de descarga: " + e.getMessage());
        }
    }
}