package org.example.asm2_insurance_claim_management_system.AdminGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Alert.ShowAlert;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;

public class retrieveClaim {
    @FXML
    private TextField textClaimId;
    @FXML
    private Button button;

    @FXML
    public void retrieveClaim(){
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        String claimId = textClaimId.getText();

        try {

            // Begin a transaction
            session.beginTransaction();

            // Load the entity you want to delete
            Claim claim = session.get(Claim.class, claimId);

            // Check if the entity exists
            if (claim != null) {
                // Load the Admin.fxml file
                // Create a new stage (window)
                displayClaimDetails(claim);
                button.getScene().getWindow().hide();


            } else {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR,"ERROR","Record with ID " + claimId + " not found.");
            }

            // Commit the transaction
            session.getTransaction().commit();
            textClaimId.clear();

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();

        }

    }
    private void displayClaimDetails(Claim claim) {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Claim Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        Label codeLabel = new Label(
                "Claim ID: " + claim.getClaimId() + "\n" +
                "Claim Date: " + claim.getClaimDate() + "\n" +
                "Claim Amount: " + claim.getClaimAmount() + "\n" +
                "List of Document: " + claim.getListOfDocument() + "\n" +
                "Claim Status: " + claim.getStatus() + "\n" +
                "Card Number: " + claim.getInsuranceCard().getCardNumber() + "\n" +
                "Policy Holder: " + claim.getPolicyHolder().getId() + "\n" +
                "Bank ID: " + claim.getBankInfo().getBankID() + "\n" +
                "Bank Name: " + claim.getBankInfo().getBankName() + "\n" +
                "Owner Name: " + claim.getBankInfo().getOwnerName() + "\n" +
                "Bank Account Number: " + claim.getBankInfo().getAccountNumber()
        );
        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBack);
        // Add the code label to the VBox
        codeContainer.getChildren().add(codeLabel);
        codeContainer.getChildren().add(returnButton);

        // Set the scene of the new stage with the VBox
        codeStage.setScene(new Scene(codeContainer, 400, 200));

        // Show the new stage
        codeStage.show();
    }
    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Load the previous FXML file (e.g., the main menu)
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/asm2_insurance_claim_management_system/Admin/Admin.fxml"));

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
