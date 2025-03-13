package gh.filesharing.client.controllers;


import at.favre.lib.crypto.bcrypt.BCrypt;
import gh.filesharing.client.Utils.AlertManager;
import gh.filesharing.client.classes.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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


        Usuario usuario = new Usuario(username, email, hashedPassword);
        listaUsuarios.add(usuario);
        AlertManager.showInfo("Usuario registrado correctamente");


    }

    public void iniciarSession(ActionEvent actionEvent) {
         String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertManager.showError("Por favor, rellene todos los campos");
            return;
        }

        // Comprobar si el usuario existe en la base de datos

        // Si el usuario no existe, mostrar un mensaje de error
        // Si el usuario existe, comprobar si la contraseña es correcta
        // Si la contraseña es correcta, mostrar un mensaje de éxito
        // Si la contraseña es incorrecta, mostrar un mensaje de error


    }
}
