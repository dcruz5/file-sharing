package gh.filesharing.client.controllers;

import com.google.gson.Gson;
import gh.filesharing.client.MainApplication;
import gh.filesharing.client.classes.CurrentSession;
import gh.filesharing.client.utils.*;
import gh.filesharing.client.classes.ApiRequest;
import io.jsonwebtoken.Claims;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

public class AuthController {

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
    ViewManager viewManager = new ViewManager(MainApplication.getStage());

    @FXML
    public void initialize() {
        viewManager = new ViewManager(MainApplication.getStage());
    }

    public void registrarUsuario(ActionEvent actionEvent) {
        String username = registerUsernameField.getText();
        String email = registerEmailField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            AlertManager.showError("Por favor, rellene todos los campos");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            AlertManager.showError("Por favor, ingrese un email válido");
            return;
        }

        if (password.length() < 6) {
            AlertManager.showError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertManager.showError("Las contraseñas no coinciden");
            return;
        }

        try {
            // TODO: should open file manager to select the path to save the private key
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // 2048-bit RSA key
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            // pk saved to user's home dir by default
            PrivateKey privateKey = keyPair.getPrivate();
            savePrivateKeyToFile(privateKey);

            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("email", email);
            params.put("password", password);
            params.put("publicKey", publicKeyString);

            String response = ApiRequest.post("/register", params);
            AlertManager.showInfo("Usuario registrado correctamente: " + response);

            registerUsernameField.clear();
            registerEmailField.clear();
            registerPasswordField.clear();
            registerConfirmPasswordField.clear();
        } catch (Exception e) {
            AlertManager.showError("No se pudo registrar el usuario: " + e.getMessage());
        }
    }

    // TODO: cuando se implemente (abrir file manager) adaptar este método
    private void savePrivateKeyToFile(PrivateKey privateKey) {
        try {
            String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            java.nio.file.Files.write(java.nio.file.Paths.get(System.getProperty("user.home") + "/private_key.pem"), privateKeyString.getBytes());
            AlertManager.showInfo("S'ha guardat la clau privada a " + System.getProperty("user.home") + "/private_key.pem");
        } catch (Exception e) {
            AlertManager.showError("Error al guardar la clau privada: " + e.getMessage());
        }
    }

    // PARA PRUEBAS:
    // username: user1, password: 12345 (NO ES ADMIN)
    public void iniciarSession(ActionEvent actionEvent) {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertManager.showError("Por favor, rellene todos los campos");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        try {
            String response = ApiRequest.post("/login", params);
            System.out.println("Login Response: " + response);

            if (response != null && response.contains("token")) {
                AlertManager.showInfo("Inicio de sesión exitoso");

                Map<String, String> responseMap = new Gson().fromJson(response, Map.class);
                String token = responseMap.get("token");

                JwtStore.saveToken(token);

                Claims claims = JwtUtil.decodeToken(token);
                if (claims != null) {
                    Long userId = claims.get("userId", Long.class);
                    String userName = (String) claims.get("sub");
                    CurrentSession currentSession = CurrentSession.getInstance();
                    currentSession.setUsername(userName);
                    currentSession.setUserId(userId);
                }

                loginUsernameField.clear();
                loginPasswordField.clear();

                viewManager.switchView("main-view.fxml", "File Sharing App");
            } else {
                AlertManager.showError("Inicio de sesión fallido: " + response);
            }
        } catch (Exception e) {
            AlertManager.showError("Error al iniciar sesión: " + e.getMessage());
        }
    }

    public void handleConnection(ActionEvent actionEvent) {
        String response = ApiRequest.get("/health");
        if (response != null) {
            if (response.equals("OK!")) {
                statusLabel.setText("Conectado al servidor SSL: " + response);
            } else {
                statusLabel.setText("Error: " + response);
            }
        } else {
            statusLabel.setText("No se pudo conectar al servidor SSL");
        }
    }
}
