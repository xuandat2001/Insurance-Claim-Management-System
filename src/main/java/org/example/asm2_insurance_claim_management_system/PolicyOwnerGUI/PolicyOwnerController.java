package org.example.asm2_insurance_claim_management_system.PolicyOwnerGUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

public class PolicyOwnerController {
    private PolicyOwner policyOwner;
    @FXML
    private Button fileClaimForPolicyHolder;
    @FXML
    private Button retrieveClaimOfPolicyHolder;
    @FXML
    private Button updateClaimOfPolicyHolder;
    @FXML
    private Button deleteClaimOfPolicyHolder;
    @FXML
    private Button fileClaimForDependent;
    @FXML
    private Button retrieveClaimOfDependent;
    @FXML
    private Button updateClaimOfDependent;
    @FXML
    private Button deleteClaimOfDependent;
    @FXML
    private Button createPolicyHolder;
    @FXML
    private Button retrieveInfoOfPolicyHolder;
    @FXML
    private Button updateInfoOfPolicyHolder;
    @FXML
    private Button deletePolicyHolder;
    @FXML
    private Button createDependent;
    @FXML
    private Button retrieveInfoOfDependent;
    @FXML
    private Button updateInfoOfDependent;
    @FXML
    private Button deleteDependent;
    @FXML
    private Button calcInsuranceFee;


    public void setPolicyOwner(PolicyOwner policyOwner) {
        this.policyOwner = policyOwner;
    }


    public void createScenePolicyOwner (String url, Button button) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));

            // Load the root element (in this case, VBox)
            VBox policyOwnerRoot = loader.load();
            CRUDForPolicyOwner crudForPolicyOwner = loader.getController();
            crudForPolicyOwner.setPolicyOwner(policyOwner);
            // Create a new stage for the Policy Owner UI
            Stage adminStage = new Stage();
            adminStage.setTitle("Policy Owner Page");
            adminStage.setScene(new Scene(policyOwnerRoot, 400, 320));

            // Show the Policy Owner UI stage
            adminStage.show();

            // Close the current stage (the one containing the button)
            button.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onFileClaimForPolicyHolder() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/fileClaimForPolicyHolder.fxml";
        createScenePolicyOwner(url, fileClaimForPolicyHolder);
    }
    @FXML
    protected void onRetrieveClaimOfPolicyHolder() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/retrieveClaimOfPolicyHolder.fxml";
        createScenePolicyOwner(url, retrieveClaimOfPolicyHolder);
    }
    @FXML
    protected void onUpdateClaimOfPolicyHolder() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/updateClaimOfPolicyHolder.fxml";
        createScenePolicyOwner(url, updateClaimOfPolicyHolder);
    }
    @FXML
    protected void onDeleteClaimOfPolicyHolder() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/deleteClaimOfPolicyHolder.fxml";
        createScenePolicyOwner(url, deleteClaimOfPolicyHolder);
    }
    @FXML
    protected void onFileClaimForDependent(){
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/fileClaimForDependent.fxml";
        createScenePolicyOwner(url, fileClaimForDependent);
    }
    @FXML
    protected void onRetrieveClaimOfDependent() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/retrieveClaimOfDependent.fxml";
        createScenePolicyOwner(url, retrieveClaimOfDependent);
    }
    @FXML
    protected void onUpdateClaimOfDependent() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/updateClaimOfDependent.fxml";
        createScenePolicyOwner(url, updateClaimOfDependent);
    }
    @FXML
    protected void onDeleteClaimOfDependent() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/deleteClaimOfDependent.fxml";
        createScenePolicyOwner(url, deleteClaimOfDependent);
    }
    @FXML
    protected void onCreatePolicyHolder() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/createPolicyHolder.fxml";
        createScenePolicyOwner(url, createPolicyHolder);
    }
    @FXML
    protected boolean onRetrieveInfoOfPolicyHolder() {
//        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/retrieveInfoOfPolicyHolder.fxml";
//        createScenePolicyOwner(url, retrieveInfoOfPolicyHolder);

        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Policy Holder Information");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        codeContainer.setPadding(new Insets(10));
        codeContainer.setSpacing(10);

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            // Begin a transaction
            session.beginTransaction();

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .getResultList();
            for (PolicyHolder policyHolder : policyHolderList) {
                Label codeLabel = new Label(
                        "Policy Holder ID: " + policyHolder.getId() + "\n" +
                                "Full Name: " + policyHolder.getFullName() + "\n" +
                                "Password: " + policyHolder.getPassword()
                );
                codeContainer.getChildren().add(codeLabel);
            }

            // Commit the transaction
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
//            session.close();
//            sessionFactory.close();
            if (session != null) {
                session.close();
            }
        }
        // Create a scene with the code container
        Scene codeScene = new Scene(codeContainer, 400, 1000);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
//    viewPolicyHolderClaimsButton.getScene().getWindow().hide();

        return false;
    }
    @FXML
    protected void onUpdateInfoOfPolicyHolder() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/updateInfoOfPolicyHolder.fxml";
        createScenePolicyOwner(url, updateInfoOfPolicyHolder);
    }
    @FXML
    protected void onDeletePolicyHolder() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/deletePolicyHolder.fxml";
        createScenePolicyOwner(url, deletePolicyHolder);
    }
    @FXML
    protected void onCreateDependent() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/createDependent.fxml";
        createScenePolicyOwner(url, createDependent);
    }
    @FXML
    protected void onRetrieveInfoOfDependent() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/retrieveInfoOfDependent.fxml";
        createScenePolicyOwner(url, retrieveInfoOfDependent);
    }
    @FXML
    protected void onUpdateInfoOfDependent() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/updateInfoOfDependent.fxml";
        createScenePolicyOwner(url, updateInfoOfDependent);
    }
    @FXML
    protected void onDeleteDependent() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/deleteDependent.fxml";
        createScenePolicyOwner(url, deleteDependent);
    }
    @FXML
    protected void onCalculateInsuranceFee() {
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyOwner/calcInsuranceFee.fxml";
        createScenePolicyOwner(url, calcInsuranceFee);
    }
}



