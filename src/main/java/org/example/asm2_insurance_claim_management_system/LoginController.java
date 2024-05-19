package org.example.asm2_insurance_claim_management_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Admin.Admin;
import org.example.asm2_insurance_claim_management_system.AdminGUI.AdminController;
import org.example.asm2_insurance_claim_management_system.Alert.ShowAlert;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.DependentGUI.DependentController;
import org.example.asm2_insurance_claim_management_system.Login.Authentication;
import org.example.asm2_insurance_claim_management_system.ManagerGUI.ManagerController;
import org.example.asm2_insurance_claim_management_system.PolicyHolderGUI.PolicyHolderController;
import org.example.asm2_insurance_claim_management_system.PolicyOwnerGUI.PolicyOwnerController;
import org.example.asm2_insurance_claim_management_system.Providers.Manager;
import org.example.asm2_insurance_claim_management_system.Providers.Surveyor;
import org.example.asm2_insurance_claim_management_system.SurveyorGUI.SurveyorController;

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
            ShowAlert showAlertSuccessfully = new ShowAlert();
            showAlertSuccessfully.showAlert(Alert.AlertType.INFORMATION,"Successful","Login Successfully");

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
                    PolicyHolderController controller = loader.getController();
                    controller.setPolicyHolder((PolicyHolder)user);
                    Stage policyHolderStage = new Stage();
                    policyHolderStage.setTitle("Policy Holder Page");
                    policyHolderStage.setScene(new Scene(policyHolderRoot, 520, 440));
                    policyHolderStage.show();
                } else if (user instanceof PolicyOwner) {
                    fxmlFile = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/PolicyOwner.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));
                    VBox policyOwnerRoot = loader.load();
                    PolicyOwnerController controller = loader.getController();
                    controller.setPolicyOwner((PolicyOwner) user);
                    Stage policyOwnerStage = new Stage();
                    policyOwnerStage.setTitle("Policy Owner Page");
                    policyOwnerStage.setScene(new Scene(policyOwnerRoot, 520, 440));
                    policyOwnerStage.show();
                }else if (user instanceof Dependent) {
                    fxmlFile = "/org/example/asm2_insurance_claim_management_system/Dependent/Dependent.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));
                    VBox policyOwnerRoot = loader.load();
                    DependentController controller = loader.getController();
                    controller.setDependent((Dependent) user);
                    Stage dependentStage = new Stage();
                    dependentStage.setTitle("Dependent Page");
                    dependentStage.setScene(new Scene(policyOwnerRoot, 520, 440));
                    dependentStage.show();
                }
                else if (user instanceof Surveyor) {
                    fxmlFile = "/org/example/asm2_insurance_claim_management_system/Surveyor/Surveyor.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));
                    VBox surveyorRoot = loader.load();
                    SurveyorController controller = loader.getController();
                    controller.setSurveyor((Surveyor) user);
                    Stage surveyorStage = new Stage();
                    surveyorStage.setTitle("Surveyor Page");
                    surveyorStage.setScene(new Scene(surveyorRoot, 520, 440));
                    surveyorStage.show();
                }
                else if (user instanceof Manager) {
                    fxmlFile = "/org/example/asm2_insurance_claim_management_system/Manager/Manager.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));
                    VBox managerControllerRoot = loader.load();
                    ManagerController controller = loader.getController();
                    controller.setManager((Manager) user);
                    Stage managerStage = new Stage();
                    managerStage.setTitle("Manager Page");
                    managerStage.setScene(new Scene(managerControllerRoot, 520, 440));
                    managerStage.show();
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