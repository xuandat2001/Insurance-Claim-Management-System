package org.example.asm2_insurance_claim_management_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Admin.Admin;
import org.example.asm2_insurance_claim_management_system.Login.Authentication;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginButton;

    @FXML
    private TextField textField;
    @FXML
    private PasswordField passwordField;

//    @FXML
//    private Button createPolicyHolderButton;

    @FXML
    protected void onLoginButtonClick() {
    String userName = textField.getText();
    String password = passwordField.getText();
    Authentication login = new Authentication();
    Admin admin = Admin.getInstance();
    if (login.authenticate(admin.listOfAdmin(), userName, password) != null){
        Alert alertSucess = new Alert(Alert.AlertType.INFORMATION);
        alertSucess.setTitle("Successful");
        alertSucess.setHeaderText(null);
        alertSucess.setContentText("Login Successfully");
        alertSucess.showAndWait();

        try {
            // Load the Admin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/asm2_insurance_claim_management_system/Admin/Admin.fxml"));

            // Load the root element (in this case, VBox)
            VBox adminRoot = loader.load();

            // Create a new stage for the Admin UI
            Stage adminStage = new Stage();
            adminStage.setTitle("Admin Page");
            adminStage.setScene(new Scene(adminRoot, 520, 440));

            // Show the Admin UI stage
            adminStage.show();

            // Close the current stage (the one containing the button)
            loginButton.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }else{
        // If the credentials are incorrect, show an alert message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText("Wrong username or password. Please try again.");
        alert.showAndWait();
    }

    }

}