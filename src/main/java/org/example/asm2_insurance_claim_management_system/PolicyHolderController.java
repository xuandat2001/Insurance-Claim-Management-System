package org.example.asm2_insurance_claim_management_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PolicyHolderController {
    @FXML
    private Button createClaimButton;

    @FXML
    protected void onCreateClaimButton(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fileClaim.fxml"));

            VBox policyHolderRoot = loader.load();

            Stage policyHolderStage = new Stage();
            policyHolderStage.setTitle("Policy Holder Page");
            policyHolderStage.setScene(new Scene(policyHolderRoot, 400, 320));

            policyHolderStage.show();

            createClaimButton.getScene().getWindow().hide();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
