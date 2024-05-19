package org.example.asm2_insurance_claim_management_system.SurveyorGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Alert.ShowAlert;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Claim.Status;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.PolicyHolderGUI.PolicyHolderClaimController;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;


public class FeaturesSurveyor {
    @FXML
    private VBox availableClaimsContainer;
    @FXML
    private TextField textClaimId;

    @FXML
    private Label availableClaims;
    @FXML
    private Button viewDocument;

    String availableClaimsList = null;

    @FXML
    private TextField textRequiredInfo;
    @FXML
    private TextField textClaimIdForDocument;


    @FXML
    private Button loadclaimsbutton;

    public boolean requireMoreInfoOnClaim() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;

        String claimId = textClaimId.getText();
        String comment = textRequiredInfo.getText();
        String availableClaimsList = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // List of documents
            String desiredClaimList = "SELECT c FROM Claim c WHERE c.status = :status";
            List<Claim> claimList = session.createQuery(desiredClaimList, Claim.class)
                    .setParameter("status", Status.NEW)
                    .getResultList();

            Claim claim = session.get(Claim.class, claimId);
            if (claim != null) {
                claim.setRequiredinfo(comment);

                ShowAlert showCommentAddedSuccessfully = new ShowAlert();
                showCommentAddedSuccessfully.showAlert(Alert.AlertType.INFORMATION,"Successful","You have successfully added the request for more information on " + claimId + " claim.");

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
    public void downloadDocument(){
        String claimId = textClaimIdForDocument.getText();

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            Claim claim = session.get(Claim.class, claimId);
            if (claim != null) {
                String fileData = claim.getListOfDocument();
                if (fileData != null && !fileData.isEmpty()) {
                    byte[] decodedFileData = Base64.getDecoder().decode(fileData);
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save PDF File");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                    fileChooser.setInitialFileName(claimId + ".pdf"); // Set the claim ID as the file name
                    File savedFile = fileChooser.showSaveDialog(new Stage());
                    if (savedFile != null) {
                        try (FileOutputStream fos = new FileOutputStream(savedFile)) {
                            fos.write(decodedFileData);
                            ShowAlert successAlert = new ShowAlert();
                            successAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "File downloaded successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                            ShowAlert errorAlert = new ShowAlert();
                            errorAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to save the file.");
                        }
                    } else {
                        ShowAlert errorAlert = new ShowAlert();
                        errorAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "No location selected to save the file");
                    }
                } else {
                    ShowAlert errorAlert = new ShowAlert();
                    errorAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "No document found in the claim.");
                }
            } else {
                ShowAlert errorAlert = new ShowAlert();
                errorAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "Claim not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ShowAlert errorAlert = new ShowAlert();
            errorAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to download the document");
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static byte[] decodeBase64ToFile(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }
    public boolean proposeClaim(){
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;

        String claimId = textClaimId.getText();

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // List of documents
            String desiredClaimList = "SELECT c FROM Claim c WHERE c.status = :status";
            List<Claim> claimList = session.createQuery(desiredClaimList, Claim.class)
                    .setParameter("status", Status.NEW)
                    .getResultList();

            Claim claim = session.get(Claim.class, claimId);
            if (claim != null) {
                ShowAlert showCommentAddedSuccessfully = new ShowAlert();
                showCommentAddedSuccessfully.showAlert(Alert.AlertType.INFORMATION,"Successful","You have successfully proposed " + claimId + " claim to the manager.");

                claim.setStatus(Status.PROCESSING);
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
    public void onViewDocument(){
        try {
            // Load the CreateClaim.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/asm2_insurance_claim_management_system/Surveyor/viewDocument.fxml"));

            // Load the root element (in this case, VBox)
            VBox root = loader.load();

            // Get the controller associated with CreateClaim.fxml

            // Create a new stage for the Create Claim UI
            Stage createClaimStage = new Stage();
            createClaimStage.setTitle("View Document");
            createClaimStage.setScene(new Scene(root, 600, 400));

            // Show the Create Claim UI stage
            createClaimStage.show();

            // Close the current stage (the one containing the button)
            viewDocument.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void initializeClaimList() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // List of documents
            String desiredClaimList = "SELECT c FROM Claim c WHERE c.status = :status";
            List<Claim> claimList = session.createQuery(desiredClaimList, Claim.class)
                    .setParameter("status", Status.NEW)
                    .getResultList();
            VBox codeContainer = new VBox();
            codeContainer.setPadding(new Insets(10));
            codeContainer.setSpacing(10);

            if (claimList.isEmpty()) {
                Label noClaimsLabel = new Label("No New Claims Available");
                codeContainer.getChildren().add(noClaimsLabel);
            } else {
                for (Claim claim : claimList) {
                    Label codeLabel = new Label(
                            "Claim ID: " + claim.getClaimId() + "\n" +
                                    "Claim Date: " + claim.getClaimDate() + "\n" +
                                    "Claim Amount: " + claim.getClaimAmount() + "\n" +
                                    "Claim Status: " + claim.getStatus() + "\n" +
                                    "Card Number: " + claim.getInsuranceCard().getCardNumber() + "\n" +
                                    "Policy Holder: " + claim.getPolicyHolder().getId() + "\n" +
                                    "Bank ID: " + claim.getBankInfo().getBankID() + "\n" +
                                    "Bank Name: " + claim.getBankInfo().getBankName() + "\n" +
                                    "Owner Name: " + claim.getBankInfo().getOwnerName() + "\n" +
                                    "Bank Account Number: " + claim.getBankInfo().getAccountNumber()
                    );
                    codeContainer.getChildren().add(codeLabel);
                }
            }
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(codeContainer);
            scrollPane.setFitToWidth(true);
            availableClaimsContainer.getChildren().clear();
            availableClaimsContainer.getChildren().add(scrollPane);

            session.getTransaction().commit();

        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Load the previous FXML file (e.g., the main menu)
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/asm2_insurance_claim_management_system/Surveyor/Surveyor.fxml"));

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

