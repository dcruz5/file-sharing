package gh.filesharing.client.controllers;

import gh.filesharing.client.utils.SSLClientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


import java.io.File;


public class UploadController {
    @FXML
    public VBox dropArea;

    @FXML
    private Button browseButton;

    @FXML
    private HBox selectedFileBox;

    @FXML
    private TextField filePathField;

    @FXML
    private Button clearFileButton;

    @FXML
    private ComboBox<String> accessTypeComboBox;

    @FXML
    private TextField accessCodeField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Label fileInfoLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Button uploadButton;

    @FXML
    private ProgressBar uploadProgressBar;

    @FXML
    private Label statusLabel;

    private File selectedFile;

    @FXML
    public void initialize() {
        setupDragAndDrop();

    }

    private void setupDragAndDrop() {
        // Cuando el archivo entra en el área de drop
        dropArea.setOnDragOver(event -> {
            if (event.getGestureSource() != dropArea && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        // Cuando se suelta el archivo en el área
        dropArea.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                ficheroSeleccionado(file);
            }
            event.setDropCompleted(true);
            event.consume();
        });


    }


    public void handleBrowseAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");

        File file = fileChooser.showOpenDialog(dropArea.getScene().getWindow());
        if (file != null) {
            ficheroSeleccionado(file);
        }

    }


    private void ficheroSeleccionado(File file) {
        selectedFile = file;
        filePathField.setText(selectedFile.getAbsolutePath());
       // fileInfoLabel.setText("Archivo seleccionado: " + selectedFile.getName());
        selectedFileBox.setVisible(true);
    }


    public void handleconecion(ActionEvent actionEvent) {
        SSLClientUtil SSLClientUtil = new SSLClientUtil();
        boolean conectado = SSLClientUtil.conectar();

        if (conectado) {
            statusLabel.setText("Conectado al servidor");
        } else {
            statusLabel.setText("No se pudo conectar al servidor");
        }
    }
}
