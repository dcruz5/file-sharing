package gh.filesharing.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VistaUtil {
     /*
     * Metodo para cambiar de vista en la aplicación
      */
    public static boolean cambiarVista(String fxmlPath, String title) {
    try {
        FXMLLoader loader = new FXMLLoader(VistaUtil.class.getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
        return true;
    } catch (Exception e) {
        System.err.println("Error al cargar vista " + fxmlPath + ": " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    public static void cerrarVentana(Stage stage) {
        stage.close();
    }

}
