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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

import gh.filesharing.client.utils.SSLClientUtil;
import gh.filesharing.client.utils.VistaUtil;

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

    private final List<Usuario> listaUsuarios = new ArrayList<>();
    private SSLClientUtil sslClientUtil;

    public void registrarUsuario(ActionEvent actionEvent) {
        String username = registerUsernameField.getText();
        String email = registerEmailField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();

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

        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        // Verificar conexión con el servidor
        if (sslClientUtil == null) {
            sslClientUtil = new SSLClientUtil();
        }

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

            Usuario usuario = new Usuario(username, email, hashedPassword);
            listaUsuarios.add(usuario);

            // Limpiar campos después del registro exitoso
            registerUsernameField.clear();
            registerEmailField.clear();
            registerPasswordField.clear();
            registerConfirmPasswordField.clear();
        } catch (Exception e) {
            AlertManager.showError("No se pudo registrar el usuario: " + e.getMessage());
        }

        // Cerrar la conexión SSL
        sslClientUtil.cerrarConexion();
    }

    private void savePrivateKeyToFile(PrivateKey privateKey) {
        try {
            String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            java.nio.file.Files.write(java.nio.file.Paths.get(System.getProperty("user.home") + "/private_key.pem"), privateKeyString.getBytes());
            AlertManager.showInfo("S'ha guardat la clau privada a " + System.getProperty("user.home") + "/private_key.pem");
        } catch (Exception e) {
            AlertManager.showError("Error al guardar la clau privada: " + e.getMessage());
        }
    }

    public void iniciarSession(ActionEvent actionEvent) {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertManager.showError("Por favor, rellene todos los campos");
            return;
        }

        if (sslClientUtil == null) {
            sslClientUtil = new SSLClientUtil();
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

                loginUsernameField.clear();
                loginPasswordField.clear();

                // cambiarVentana();
                sslClientUtil.cerrarConexion();

//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
//                Parent mainView = loader.load();
//
//                Stage stage = (Stage) loginUsernameField.getScene().getWindow();
//                stage.setScene(new Scene(mainView));
//                stage.show();
            } else {
                AlertManager.showError("Inicio de sesión fallido: " + response);
            }
        } catch (Exception e) {
            AlertManager.showError("Error al iniciar sesión: " + e.getMessage());
        }
    }

    private void cambiarVentana()  {
        try {
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            boolean vistaExitosa = VistaUtil.cambiarVista("/gh/filesharing/client/view/main-view.fxml", "File Sharing App");
            if (!vistaExitosa) {
                AlertManager.showError("Error al cambiar de vista");
                return;
            }
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
