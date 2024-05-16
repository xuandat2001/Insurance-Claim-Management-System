package org.example.asm2_insurance_claim_management_system. AdminGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Admin.Admin;

import java.io.IOException;

public class AdminController{
    private Admin admin;
    @FXML
    private Button createPolicyHolderButton;
    @FXML
    private Button updatePolicyHolderButton;

    @FXML
    private Button deletePolicyHolderButton;
    @FXML
    private Button viewPolicyHolderButton;
    @FXML
    private Button createPolicyOwnerButton;
    @FXML
    private Button updatePolicyOwnerButton;
    @FXML
    private Button deletePolicyOwnerButton;
    @FXML
    private Button viewPolicyOwnerButton;
    @FXML
    private Button createDependentButton;
    @FXML
    private Button deleteDependentButton;
    @FXML
    private Button updateDependentButton;
    @FXML
    private Button viewDependentButton;
    @FXML
    private Button createSurveyorButton;
    @FXML
    private Button updateSurveyorButton;
    @FXML
    private Button deleteSurveyorButton;
    @FXML
    private Button viewSurveyorButton;
    @FXML
    private Button createManagerButton;
    @FXML
    private Button updateManagerButton;
    @FXML
    private Button deleteManagerButton;
    @FXML
    private Button viewManagerButton;
    public void setAdmin(Admin admin) {
        this.admin = admin;
        // Optionally update the UI or other components with admin data
    }
    public void createSceneAdmin (String url, Button button) {
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
    @FXML
    protected void OnCreatePolicyHolderButton() {
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/createPolicyHolder.fxml";
        createSceneAdmin(url, createPolicyHolderButton);
    }
    @FXML
    protected void OnUpdatePolicyHolderButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/updatePolicyHolder.fxml";
        createSceneAdmin(url, updatePolicyHolderButton);
    }
    @FXML
    protected void OnDeletePolicyHolderButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/deletePolicyHolder.fxml";
        createSceneAdmin(url, deletePolicyHolderButton);
    }
    @FXML
    protected void OnViewPolicyHolderButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/viewPolicyHolder.fxml";
        createSceneAdmin(url, viewPolicyHolderButton);
    }

    @FXML
    protected void OnCreatePolicyOwnerButton() {
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/createPolicyOwner.fxml";
        createSceneAdmin(url,createPolicyOwnerButton);
    }
    @FXML
    protected void OnUpdatePolicyOwnerButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/updatePolicyOwner.fxml";
        createSceneAdmin(url, updatePolicyOwnerButton);
    }
    @FXML
    protected void OnDeletePolicyOwnerButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/deletePolicyOwner.fxml";
        createSceneAdmin(url, deletePolicyOwnerButton);
    }
    @FXML
    protected void OnViewPolicyOwnerButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/viewPolicyOwner.fxml";
        createSceneAdmin(url, viewPolicyOwnerButton);
    }
    @FXML
    protected void OnCreateDependentButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/createDependent.fxml";
        createSceneAdmin(url, createDependentButton);
    }
    @FXML
    protected void OnDeleteDependentButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/deleteDependent.fxml";
        createSceneAdmin(url, deleteDependentButton);
    }
    @FXML
    protected void OnUpdateDependentButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/updateDependent.fxml";
        createSceneAdmin(url, updateDependentButton);
    }
    @FXML
    protected void OnViewDependentButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/viewDependent.fxml";
        createSceneAdmin(url, viewDependentButton);
    }
    @FXML
    protected void OnCreateSurveyorButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/createSurveyor.fxml";
        createSceneAdmin(url, createSurveyorButton);
    }

    @FXML
    protected void OnUpdateSurveyorButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/updateSurveyor.fxml";
        createSceneAdmin(url, updateSurveyorButton);
    }
    @FXML
    protected void OnDeleteSurveyorButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/deleteSurveyor.fxml";
        createSceneAdmin(url, deleteSurveyorButton);
    }
    @FXML
    protected void OnViewSurveyorButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/viewSurveyor.fxml";
        createSceneAdmin(url, viewSurveyorButton);
    }
    @FXML
    protected void OnCreateManagerButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/CreateManager.fxml";
        createSceneAdmin(url, createManagerButton);
    }
    @FXML
    protected void OnUpdateManagerButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/updateManager.fxml";
        createSceneAdmin(url, updateManagerButton);
    }
    @FXML
    protected void OnDeleteManagerButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/deleteManager.fxml";
        createSceneAdmin(url, deleteManagerButton);
    }
    @FXML
    protected void OnViewManagerButton(){
        String url = "/org/example/asm2_insurance_claim_management_system/Admin/viewManager.fxml";
        createSceneAdmin(url, viewManagerButton);
    }
    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Load the previous FXML file (e.g., the main menu)
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/asm2_insurance_claim_management_system/login.fxml"));

            // Get the source node of the event (the button)
            Node source = (Node) event.getSource();

            // Get the current stage (window)
            Stage stage = (Stage) source.getScene().getWindow();

            // Set the scene to the new root (previous page)
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
