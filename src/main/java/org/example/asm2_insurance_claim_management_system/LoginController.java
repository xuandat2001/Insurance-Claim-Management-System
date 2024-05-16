package org.example.asm2_insurance_claim_management_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Admin.Admin;
import org.example.asm2_insurance_claim_management_system.AdminGUI.AdminController;
import org.example.asm2_insurance_claim_management_system.Alert.ShowAlert;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.Login.AdminControllerLogin;
import org.example.asm2_insurance_claim_management_system.Login.Authentication;
import org.example.asm2_insurance_claim_management_system.Login.PolicyHolderControllerLogin;
import org.example.asm2_insurance_claim_management_system.Login.PolicyOwnerControllerLogin;
import org.example.asm2_insurance_claim_management_system.PolicyHolderGUI.FeaturesPolicyHolder;

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
    protected void onLoginButtonClick() throws IOException {
        String userName = textField.getText();
        String password = passwordField.getText();
        Authentication login = new Authentication();

        Object user = login.authenticate(userName, password);

        if (user != null) {
            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
            alertSuccess.setTitle("Successful");
            alertSuccess.setHeaderText(null);
            alertSuccess.setContentText("Login Successfully");
            alertSuccess.showAndWait();

            try {
                FXMLLoader loader = new FXMLLoader();
                String fxmlFile = "";

                if (user instanceof Admin) {
                    fxmlFile = "/org/example/asm2_insurance_claim_management_system/Admin/Admin.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));

                    VBox adminRoot = loader.load();
                    AdminController controller = loader.getController();
                    controller.setAdmin((Admin)user);
                    //System.out.println(loader.getController().getClass().getName());
                    Stage adminStage = new Stage();
                    adminStage.setTitle("Admin Page");
                    adminStage.setScene(new Scene(adminRoot, 520, 440));
                    adminStage.show();
                } else if (user instanceof PolicyHolder) {
                    fxmlFile = "/org/example/asm2_insurance_claim_management_system/PolicyHolder/PolicyHolder.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));
                    VBox policyHolderRoot = loader.load();
                    FeaturesPolicyHolder controller = loader.getController();
                    controller.setPolicyHolder((PolicyHolder)user);
                    Stage policyHolderStage = new Stage();
                    policyHolderStage.setTitle("Policy Holder Page");
                    policyHolderStage.setScene(new Scene(policyHolderRoot, 520, 440));
                    policyHolderStage.show();
                } else if (user instanceof PolicyOwner) {
                    fxmlFile = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/PolicyOwner.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));
                    VBox policyOwnerRoot = loader.load();
                    //PolicyOwnerControllerLogin controller = loader.getController();
                    //controller.setPolicyOwner((PolicyOwner) user);
                    Stage policyOwnerStage = new Stage();
                    policyOwnerStage.setTitle("Policy Owner Page");
                    policyOwnerStage.setScene(new Scene(policyOwnerRoot, 520, 440));
                    policyOwnerStage.show();
                }

                // Close the current stage (the one containing the button)
                loginButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // If the credentials are incorrect, show an alert message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Wrong username or password. Please try again.");
            alert.showAndWait();
        }
    }
}