package org.example.asm2_insurance_claim_management_system.SurveyorGUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Providers.Surveyor;

import java.io.IOException;

public class SurveyorController {
    private Surveyor surveyor;

    @FXML
    private Button ProposeClaimFormButton;

    @FXML
    private Button RequireMoreInfoOnClaimButton;

    public void setSurveyor(Surveyor surveyor) {
        this.surveyor = surveyor;
    }

    @FXML
    public void proposeClaimForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/Surveyor/proposeClaimForm.fxml";
        createSceneSurveyor(url, ProposeClaimFormButton);
    }

    @FXML
    public void requireMoreInfoForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/Surveyor/requireMoreInfoForm.fxml";
        createSceneSurveyor(url, RequireMoreInfoOnClaimButton);
    }

    public void createSceneSurveyor (String url, Button button) {
        try {
            // Load the Admin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));

            // Load the root element (in this case, VBox)
            VBox surveyorRoot = loader.load();

            // Create a new stage for the Surveyor UI
            Stage surveyorStage = new Stage();
            surveyorStage.setTitle("Surveyor Page");
            surveyorStage.setScene(new Scene(surveyorRoot, 500, 800));

            // Show the Surveyor UI stage
            surveyorStage.show();

            // Close the current stage (the one containing the button)
            button.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


