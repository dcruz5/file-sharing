package gh.filesharing.client.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import gh.filesharing.client.utils.AlertManager;
import gh.filesharing.client.utils.JwtStore;
import gh.filesharing.client.utils.ApiRequest;
import gh.filesharing.client.classes.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private final List<Usuario> listaUsuarios = new ArrayList<>();

    public void registrarUsuario(ActionEvent actionEvent) {
        String username = registerUsernameField.getText();
        String email = registerEmailField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            AlertManager.showError("Por favor, rellene todos los campos");
            return;
        }
        if (!password.equals(confirmPassword)) {
            AlertManager.showError("Las contraseñas no coinciden");
            return;
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // 2048-bit RSA key
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Get the public key and encode it as a string
            PublicKey publicKey = keyPair.getPublic();
            String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            // Save the private key to a file on the client's PC
            PrivateKey privateKey = keyPair.getPrivate();
            savePrivateKeyToFile(privateKey);

            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("email", email);
            params.put("passwordHash", hashedPassword);
            params.put("publicKey", publicKeyString);

            String response = ApiRequest.post("/register", params);
            AlertManager.showInfo("Usuario registrado correctamente: " + response);
        } catch (Exception e) {
            AlertManager.showError("Error al registrar el usuario: " + e.getMessage());
        }
    }

    private void savePrivateKeyToFile(PrivateKey privateKey) {
        try {
            // You can use File I/O to save the private key to a file on the user's system
            String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            // Save to file (for simplicity, we're assuming it's being saved in the user's home directory)
            java.nio.file.Files.write(java.nio.file.Paths.get(System.getProperty("user.home") + "/private_key.pem"), privateKeyString.getBytes());
            AlertManager.showInfo("Private key saved to your computer.");
        } catch (Exception e) {
            AlertManager.showError("Error saving private key: " + e.getMessage());
        }
    }

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

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
                Parent mainView = loader.load();

                Stage stage = (Stage) loginUsernameField.getScene().getWindow();
                stage.setScene(new Scene(mainView));
                stage.show();
            } else {
                AlertManager.showError("Inicio de sesión fallido: " + response);
            }
        } catch (Exception e) {
            AlertManager.showError("Error al iniciar sesión: " + e.getMessage());
        }
    }
}
