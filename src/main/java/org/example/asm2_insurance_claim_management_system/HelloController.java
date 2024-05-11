package org.example.asm2_insurance_claim_management_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Admin.Admin;
import org.example.asm2_insurance_claim_management_system.Login.Authentication;

import java.io.IOException;
import java.sql.Statement;

public class HelloController {
    @FXML
    private Button loginButton;

    @FXML
    private TextField textField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button createPolicyHolderButton;
    @FXML
    protected void onLoginButtonClick() {
    String userName = textField.getText();
    String password = passwordField.getText();
    Authentication login = new Authentication();
    Admin admin = Admin.getInstance();
    if (login.authenticate(admin.listOfAdmin(), userName, password) != null){
        System.out.println("Login successfully (Admin)");
        try {
            // Load the Admin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));

            // Load the root element (in this case, VBox)
            VBox adminRoot = loader.load();

            // Create a new stage for the Admin UI
            Stage adminStage = new Stage();
            adminStage.setTitle("Admin Page");
            adminStage.setScene(new Scene(adminRoot, 320, 240));

            // Show the Admin UI stage
            adminStage.show();

            // Close the current stage (the one containing the button)
            loginButton.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }
}