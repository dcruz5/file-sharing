package gh.filesharing.client.controllers;

import gh.filesharing.client.MainApplication;
import gh.filesharing.client.classes.CurrentSession;
import gh.filesharing.client.utils.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import gh.filesharing.client.utils.AlertManager;

public class MainController {
    public Label usernameLabel;
    private static final ViewManager viewManager = new ViewManager(MainApplication.getStage());

    @FXML
    public void initialize() {
        CurrentSession session = CurrentSession.getInstance();
        System.out.println("SESSION: " + session);

        if (session == null || session.getUserId() == null) {
            System.out.println("No se pudo obtener la sesión actual.");
            return;
        }

        long userId = session.getUserId();
//        fetchUserData(userId);
    }

    public void handleProfileAction(ActionEvent actionEvent) {
        viewManager.switchView("main-view.fxml", "Mi Perfil");
    }

    public void handleLogoutAction(ActionEvent actionEvent) {
//        CurrentSession session = CurrentSession.getInstance();
//        session.clearSession();
        AlertManager.showInfo("Sesión cerrada correctamente.");
        viewManager.switchView("login-register-view.fxml", "Login");
    }

    public void handleNewUploadAction(ActionEvent actionEvent) {
        viewManager.switchView("upload-view.fxml", "Subir Nuevo Archivo");
    }

    public void handleDownloadFileAction(ActionEvent actionEvent) {
        viewManager.switchView("download-view.fxml", "Descargar Archivo");
    }
}