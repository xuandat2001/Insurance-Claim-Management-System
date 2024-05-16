package org.example.asm2_insurance_claim_management_system.PolicyHolderGUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;

import java.io.IOException;

public class PolicyHolderController {

    @FXML
    private Button RetrieveInfoPolicyHolderButton;



    public void createScenePolicyHolder (String url, Button button) {
        try {
            // Load the Admin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));

            // Load the root element (in this case, VBox)
            VBox adminRoot = loader.load();

            // Create a new stage for the Admin UI
            Stage adminStage = new Stage();
            adminStage.setTitle("Admin Page");
            adminStage.setScene(new Scene(adminRoot, 400, 320));

            // Show the Admin UI stage
            adminStage.show();

            // Close the current stage (the one containing the button)
            button.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
