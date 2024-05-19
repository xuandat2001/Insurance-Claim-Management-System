package org.example.asm2_insurance_claim_management_system.ManagerGUI;
/**
 * @author <Group 22>
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Alert.ShowAlert;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Claim.Status;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

public class FeaturesManager {
    @FXML
    private TextField textClaimId;

    @FXML
    private Label availableClaims;

    String availableClaimsList = null;

    @FXML
    private TextField textReasonForRejection;

    public boolean rejectClaim() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;

        String claimId = textClaimId.getText();
        String comment = textReasonForRejection.getText();

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // List of documents
            String desiredClaimList = "SELECT c FROM Claim c WHERE c.status = :status";
            List<Claim> claimList = session.createQuery(desiredClaimList, Claim.class)
                    .setParameter("status", Status.PROCESSING)
                    .getResultList();

            Claim claim = session.get(Claim.class, claimId);
            if (claim != null) {
                claim.setStatus(Status.DONE);
                claim.setRequiredinfo("Manager: " + comment);
                claim.setApproval("NO");

                ShowAlert showCommentAddedSuccessfully = new ShowAlert();
                showCommentAddedSuccessfully.showAlert(Alert.AlertType.INFORMATION,"Successful","You have successfully rejected " + claimId + " claim.");

                session.getTransaction().commit();
                return true; // Update successful
            } else {
                ShowAlert showCommentAddedSuccessfully = new ShowAlert();
                showCommentAddedSuccessfully.showAlert(Alert.AlertType.INFORMATION,"Error",claimId + " claim was not found. Please enter the correct 6-digit claimID");

                session.getTransaction().rollback();
                return false; // Claim not found
            }
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            ex.printStackTrace(); // Print error details
            return false; // Update failed
        } finally {
            // Close the session
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean approveClaim(){
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;

        String claimId = textClaimId.getText();

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // List of documents
            String desiredClaimList = "SELECT c FROM Claim c WHERE c.status = :status";
            List<Claim> claimList = session.createQuery(desiredClaimList, Claim.class)
                    .setParameter("status", Status.PROCESSING)
                    .getResultList();

            Claim claim = session.get(Claim.class, claimId);
            if (claim != null) {
                ShowAlert showCommentAddedSuccessfully = new ShowAlert();
                showCommentAddedSuccessfully.showAlert(Alert.AlertType.INFORMATION,"Successful","You have successfully approved " + claimId);

                claim.setStatus(Status.DONE);
                claim.setApproval("YES");

                session.getTransaction().commit();
                return true; // Update successful
            } else {
                ShowAlert showCommentAddedSuccessfully = new ShowAlert();
                showCommentAddedSuccessfully.showAlert(Alert.AlertType.INFORMATION,"Error",claimId + " claim was not found. Please enter the correct 6-digit claimID");
                session.getTransaction().rollback();
                return false; // Claim not found
            }
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            ex.printStackTrace(); // Print error details
            return false; // Update failed
        } finally {
            // Close the session
            if (session != null) {
                session.close();
            }
        }
    }

    public void initializeProcessingClaimList() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // List of documents
            String desiredClaimList = "SELECT c FROM Claim c WHERE c.status = :status";
            List<Claim> claimList = session.createQuery(desiredClaimList, Claim.class)
                    .setParameter("status", Status.PROCESSING)
                    .getResultList();

            for (Claim claim : claimList) {
                availableClaimsList = claimList.toString();
            }
            if (availableClaimsList == null){
                availableClaims.setText("No Processing Claim Available to Approve or Reject.");
            } else {
                availableClaims.setText(availableClaimsList);
            }
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Load the previous FXML file (e.g., the main menu)
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/asm2_insurance_claim_management_system/Manager/Manager.fxml"));

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
