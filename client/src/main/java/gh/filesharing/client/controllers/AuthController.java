package gh.filesharing.client.controllers;


import at.favre.lib.crypto.bcrypt.BCrypt;
import gh.filesharing.client.utils.AlertManager;
import gh.filesharing.client.utils.ApiClient;
import gh.filesharing.client.classes.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<Usuario> listaUsuarios=new ArrayList<>() ;

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

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("email", email);
        params.put("password", hashedPassword);

        try {
            String response = ApiClient.post("/users", params);
            AlertManager.showInfo("Usuario registrado correctamente: " + response);
        } catch (IOException e) {
            AlertManager.showError("Error al registrar el usuario: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void iniciarSession(ActionEvent actionEvent) throws Exception {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertManager.showError("Por favor, rellene todos los campos");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        String res = ApiClient.get("/health");
        System.out.println(res);

        try {
            String response = ApiClient.post("/login", params);
            AlertManager.showInfo("Inicio de sesión exitoso: " + response);
        } catch (Exception e) {
            AlertManager.showError("Error al iniciar sesión: " + e.getMessage());
        }
    }

}
