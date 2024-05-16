package org.example.asm2_insurance_claim_management_system.PolicyHolderGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

public class FeaturesPolicyHolder {
    private PolicyHolder policyHolder;

    @FXML
    private Button viewPolicyHolderButton;
    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    @FXML
    public void retrieveInfoPolicyHolder(){

        // Create a Hibernate SessionFactory
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();


        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            if (policyHolder != null) {
                displayPolicyHolderDetails(policyHolder);
                viewPolicyHolderButton.getScene().getWindow().hide();
            }

            // Commit the transaction
            session.getTransaction().commit();

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();
            sessionFactory.close();
        }


    }
    private void displayPolicyHolderDetails(PolicyHolder policyHolder) {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("PolicyHolder Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        Label codeLabel = new Label("PolicyHolder: " + "\n" +
                "PolicyHolderId: " + policyHolder.getId() + "'\n" +
                "Password: " + policyHolder.getPassword() + "'\n" +
                "FullName: " + policyHolder.getFullName() + "'\n" +
                "InsuranceCard: " + policyHolder.getInsuranceCard().getCardNumber() + "\n" +
                "PolicyOwner: " + policyHolder.getPolicyOwner().getFullName()
        );

        // Add the code label to the VBox
        codeContainer.getChildren().add(codeLabel);

        // Set the scene of the new stage with the VBox
        codeStage.setScene(new Scene(codeContainer, 400, 200));

        // Show the new stage
        codeStage.show();
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
