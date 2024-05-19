package org.example.asm2_insurance_claim_management_system.DependentGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

public class DependentController {
    //Attributes for retrieveClaim
    @FXML private Button retrieveClaim;

    //Attributes for ShowInfo()
    @FXML private Button showInfo;
    private Dependent dependent;

    public void setDependent(Dependent dependent) {
        this.dependent = dependent;
    }

    public boolean retrieveClaim() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            String desiredClaim = "SELECT c FROM Claim c WHERE c.dependent IS NOT NULL";
            List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                    .getResultList();
            retrieveDependentDetailsClaim(claimList);
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();

        }
        return false;
    }
    private void retrieveDependentDetailsClaim(List<Claim> claimList) {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Dependent Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        codeContainer.setPadding(new Insets(10));
        codeContainer.setSpacing(10);

        for (Claim claim : claimList) {
            if (dependent.getId().equals(claim.getDependent().getId())) {
                Label codeLabel = new Label(
                        "Claim ID: " + claim.getClaimId() + "\n" +
                                "Claim Date: " + claim.getClaimDate() + "\n" +
                                "Claim Amount: " + claim.getClaimAmount() + "\n" +
                                "List of Document: " + claim.getListOfDocument() + "\n" +
                                "Claim Status: " + claim.getStatus() + "\n" +
                                "Card Number: " + claim.getInsuranceCard().getCardNumber() + "\n" +
                                "Policy Holder: " + claim.getPolicyHolder().getId() + "\n" +
                                "Dependent: " + claim.getDependent().getId() + "\n" +
                                "Bank ID: " + claim.getBankInfo().getBankID() + "\n" +
                                "Bank Name: " + claim.getBankInfo().getBankName() + "\n" +
                                "Owner Name: " + claim.getBankInfo().getOwnerName() + "\n" +
                                "Bank Account Number: " + claim.getBankInfo().getAccountNumber()
                );
                codeContainer.getChildren().add(codeLabel);
            }
        }

        // Create a scene with the code container
        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBackMainMenu);
        // Add the Close button to the VBox
        codeContainer.getChildren().add(returnButton);
        Scene codeScene = new Scene(codeContainer, 400, 300);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
        retrieveClaim.getScene().getWindow().hide();
    }
    public boolean showInfo(){
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<Dependent> dependentList;
        try {
            // Begin a transaction
            session.beginTransaction();
            displayDependentDetails();
            // Commit the transaction
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();

        }
        return false;
    }
    private void displayDependentDetails() {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Dependent Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        Label codeLabel = new Label("PolicyHolder: " + "\n" +
                "DependentId: " + dependent.getId() + "'\n" +
                "Password: " + dependent.getPassword() + "'\n" +
                "FullName: " + dependent.getFullName() + "'\n" +
                "PolicyHolder: " + dependent.getPolicyHolder().getFullName() + "\n" +
                "PolicyOwner: " + dependent.getPolicyOwner().getFullName()
        );

        // Add the code label to the VBox
        codeContainer.getChildren().add(codeLabel);
        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBackMainMenu);
        // Add the Close button to the VBox
        codeContainer.getChildren().add(returnButton);
        // Set the scene of the new stage with the VBox
        codeStage.setScene(new Scene(codeContainer, 400, 200));

        // Show the new stage
        codeStage.show();
        // Hide the current window
        showInfo.getScene().getWindow().hide();
    }
    @FXML
    private void goBackMainMenu(ActionEvent event) {
        try {
            // Load the previous FXML file (e.g., the main menu)
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/asm2_insurance_claim_management_system/Dependent/Dependent.fxml"));

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
