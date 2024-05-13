package org.example.asm2_insurance_claim_management_system.Alert;

import javafx.scene.control.Alert;

public class ShowAlert {
    public void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
