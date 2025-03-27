package gh.filesharing.client.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import gh.filesharing.client.Utils.AlertManager;
import gh.filesharing.client.Utils.SSLClientUtil;
import gh.filesharing.client.Utils.VistaUtil;
import gh.filesharing.client.classes.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginRegisterController {

    @FXML
    public TextField registerEmailField;
    @FXML
    public TextField registerUsernameField;
    @FXML
    public PasswordField registerPasswordField;
    @FXML
    public PasswordField registerConfirmPasswordField;
    @FXML
    public TextField loginUsernameField;
    @FXML
    public PasswordField loginPasswordField;
    @FXML
    public Label statusLabel;
    @FXML
    public Button loginButton;

    private SSLClientUtil sslClientUtil;

    @FXML
    public void registrarUsuario() {
        String username = registerUsernameField.getText().trim();
        String email = registerEmailField.getText().trim();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();

        // Validaciones básicas
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            AlertManager.showError("Por favor, rellene todos los campos");
            return;
        }

        // Validación de formato de email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            AlertManager.showError("Por favor, ingrese un email válido");
            return;
        }

        // Validación de longitud de contraseña
        if (password.length() < 6) {
            AlertManager.showError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertManager.showError("Las contraseñas no coinciden");
            return;
        }

        // Verificar conexión con el servidor
        if (sslClientUtil == null) {
            sslClientUtil = new SSLClientUtil();
        }

        // Hash de la contraseña
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        // Intentar registro
        boolean registrado = sslClientUtil.registrarUsuario(username, email, hashedPassword);

        if (registrado) {
            AlertManager.showInfo("Usuario registrado correctamente");
            // Limpiar campos después del registro exitoso
            registerUsernameField.clear();
            registerEmailField.clear();
            registerPasswordField.clear();
            registerConfirmPasswordField.clear();
        } else {
            AlertManager.showError("No se pudo registrar el usuario. Por favor, intente nuevamente.");
        }

        // Cerrar la conexión SSL
        sslClientUtil.cerrarConexion();
    }

    public void iniciarSession(ActionEvent actionEvent) throws IOException {
        String username = loginUsernameField.getText().trim();
        String password = loginPasswordField.getText();

        // Validaciones básicas
        if (username.isEmpty() || password.isEmpty()) {
            AlertManager.showError("Por favor, rellene todos los campos");
            return;
        }

        // Verificar conexión con el servidor
        if (sslClientUtil == null) {
            sslClientUtil = new SSLClientUtil();
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());


        // Intentar login
       // boolean loginExitoso = sslClientUtil.autenticarUsuario(username, hashedPassword);
boolean loginExitoso = true;
        if (loginExitoso) {
            AlertManager.showInfo("Inicio de sesión exitoso");
            // Limpiar campos
            loginUsernameField.clear();
            loginPasswordField.clear();
            // cambiar la vista
            cambiarVentana();
             } else {
            AlertManager.showError("Usuario o contraseña incorrectos");
        }

        // Cerrar la conexión SSL
        sslClientUtil.cerrarConexion();
    }

private void cambiarVentana()  {
        try {

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            // Cargar la nueva vista primero
            boolean vistaExitosa = VistaUtil.cambiarVista("/gh/filesharing/client/view/main-view.fxml", "File Sharing App");
            if (!vistaExitosa) {
                AlertManager.showError("Error al cambiar de vista");
                return;
            }

            // Cerrar la ventana actual después
            VistaUtil.cerrarVentana(currentStage);
        } catch (Exception e) {
            AlertManager.showError("Error al cambiar de vista: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void handleconecion(ActionEvent actionEvent) {
        if (sslClientUtil == null) {
            sslClientUtil = new SSLClientUtil();
        }

        boolean conectado = sslClientUtil.conectar();

        if (conectado) {
            statusLabel.setText("Conectado al servidor SSL");
        } else {
            statusLabel.setText("No se pudo conectar al servidor SSL");
        }

        // Cerrar la conexión SSL después de verificar
        sslClientUtil.cerrarConexion();
    }
}
