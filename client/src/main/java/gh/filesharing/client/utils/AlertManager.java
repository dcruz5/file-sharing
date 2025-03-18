package gh.filesharing.client.utils;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class AlertManager {

    // Cargar el estilo de FlatLaf
    static {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mostrar alerta con FlatLaf
    public static void showAlert(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    // Métodos específicos para tipos de alertas
    public static void showInfo(String message) {
        showAlert("Información", message, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarning(String message) {
        showAlert("Advertencia", message, JOptionPane.WARNING_MESSAGE);
    }

    public static void showError(String message) {
        showAlert("Error", message, JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showConfirm(String message) {
        int result = JOptionPane.showConfirmDialog(null, message, "Confirmación", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}
