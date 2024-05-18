package org.example.asm2_insurance_claim_management_system.ManagerGUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Providers.Manager;

import java.io.IOException;

public class ManagerController {
    private Manager manager;

    @FXML
    private Button ApproveClaimFormButton;

    @FXML
    private Button RejectClaimFormButton;

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @FXML
    public void approveClaimForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/Manager/approveClaimForm.fxml";
        createSceneManager(url, ApproveClaimFormButton);
    }

    @FXML
    public void rejectClaimForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/Manager/rejectClaimForm.fxml";
        createSceneManager(url, RejectClaimFormButton);
    }

    public void createSceneManager (String url, Button button) {
        try {
            // Load the Manager.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));

            // Load the root element (in this case, VBox)
            VBox managerRoot = loader.load();

            // Create a new stage for the Manager UI
            Stage managerStage = new Stage();
            managerStage.setTitle("Manager Page");
            managerStage.setScene(new Scene(managerRoot, 500, 800));

            // Show the Manager UI stage
            managerStage.show();

            // Close the current stage (the one containing the button)
            button.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
