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
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CRUDPolicyHolderController implements CRUDoperation {
    @FXML
    private Button backButton;
    // Attributes of create()
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldFullName;
    @FXML
    private TextField textCardNum;
    @FXML
    private TextField textFieldPolicyOwner;
    @FXML
    private TextField textFieldDate;


    // Attributes of update()
    @FXML
    private TextField checkUpdateId;
    @FXML
    private TextField updatePassword;
    @FXML
    private TextField updateFullName;

    // Attributes of delete()
    @FXML
    private TextField deletePolicyHolder;
    // Attributes of view()
    @FXML
    private TextField viewPolicyHolder;
    @FXML
    private Button viewPolicyHolderButton;


    @FXML
    @Override
    public boolean createEntity() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        String userName = textFieldId.getText();
        String password = textFieldPassword.getText();
        String fullName = textFieldFullName.getText();
        String cardNum = textCardNum.getText();
        String policyOwnerId = textFieldPolicyOwner.getText();
        String userExpirationDate = textFieldDate.getText();
        if (userName.isEmpty() || password.isEmpty() || fullName.isEmpty() || cardNum.isEmpty() || policyOwnerId.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expirationDate = null;
        try {
            // Parse the user input into a LocalDate object using the defined format
            expirationDate = LocalDate.parse(userExpirationDate, dateFormatter);

            // Print the parsed LocalDate object
            System.out.println("Parsed date: " + expirationDate);
        } catch (Exception e) {
            // If the credentials are incorrect, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Date Error", "Invalid date format. Please enter date in YYYY-MM-DD format.");
            return false;
        }

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredOwner = "SELECT o FROM PolicyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyOwner> policyOwnerList = session.createQuery(desiredOwner, PolicyOwner.class)
                .setParameter("policyOwnerId", policyOwnerId)
                .getResultList();

        if (policyOwnerList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Owner does not exist");
            return false;
        }
        PolicyOwner policyOwner = policyOwnerList.get(0);
        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId AND h.id = :policyHolderID";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwnerId)
                .setParameter("policyHolderID", userName)
                .getResultList();

        if (!policyHolderList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder has been created");
            return false;
        }

        List<InsuranceCard> insuranceCardList = session.createQuery("FROM InsuranceCard WHERE cardNumber = :cardNum", InsuranceCard.class)
                .setParameter("cardNum", cardNum)
                .getResultList();

        if (!insuranceCardList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Card Number has been existed.");
            return false;
        }
        InsuranceCard insuranceCard = new InsuranceCard();
        insuranceCard.setCardNumber(cardNum);
        insuranceCard.setExpirationDate(expirationDate);
        insuranceCard.setCardHolder(userName);
        insuranceCard.setPolicyOwner(policyOwner);
        PolicyHolder policyHolder = new PolicyHolder();
        policyHolder.setCustomerId(userName);
        policyHolder.setPassword(password);
        policyHolder.setFullName(fullName);
        policyHolder.setPolicyOwner(policyOwner);

        // Set InsuranceCard for PolicyHolder
        policyHolder.setInsuranceCard(insuranceCard);

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            session.save(insuranceCard);
            session.save(policyHolder);

            // Commit the transaction
            session.getTransaction().commit();
            //Alert
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Create Successfully");

            //clear field
            textFieldId.clear();
            textFieldPassword.clear();
            textFieldFullName.clear();
            textCardNum.clear();
            textFieldPolicyOwner.clear();
            textFieldDate.clear();
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

    @Override
    public boolean updateEntity() {
        String userName = checkUpdateId.getText();
        String newPassword = updatePassword.getText();
        String newFullName = updateFullName.getText();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h WHERE h.id = :policyHolderID";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyHolderID", userName)
                    .getResultList();

            if (policyHolderList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist");
                return false;
            }
            PolicyHolder policyHolder = policyHolderList.get(0);
            // Update only non-empty fields
            if (!newPassword.isEmpty()) {
                policyHolder.setPassword(newPassword);
            }
            if (!newFullName.isEmpty()) {
                policyHolder.setFullName(newFullName);
            }

            // Commit the transaction
            session.getTransaction().commit();

            // alert
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Update Successfully");
            //clear the field
            checkUpdateId.clear();
            updatePassword.clear();
            updateFullName.clear();
            return true; // Update successful
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            ex.printStackTrace(); // Print error details
            return false; // Update failed
        } finally {
            // Close the session and session factory
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteEntity() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        String userName = deletePolicyHolder.getText();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();


        try {
            // Begin a transaction
            session.beginTransaction();

            // Load the entity you want to delete
            PolicyHolder policyHolder = session.get(PolicyHolder.class, userName);

            // Check if the entity exists
            if (policyHolder != null) {
                // Delete the entity
                session.delete(policyHolder);
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Record deleted successfully.");


            } else {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "Policy Holder with ID " + userName + " not found.");
            }

            // Commit the transaction
            session.getTransaction().commit();
            deletePolicyHolder.clear();
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

    @Override
    public boolean viewEntity() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        String userName = viewPolicyHolder.getText();

        try {

            // Begin a transaction
            session.beginTransaction();

            // Load the entity you want to delete
            PolicyHolder policyHolder = session.get(PolicyHolder.class, userName);

            // Check if the entity exists
            if (policyHolder != null) {
                // Load the Admin.fxml file
                // Create a new stage (window)
                displayPolicyHolderDetails(policyHolder);
                viewPolicyHolderButton.getScene().getWindow().hide();


            } else {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "Policy Holder with ID " + userName + " not found.");
            }

            // Commit the transaction
            session.getTransaction().commit();
            viewPolicyHolder.clear();
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


    // Method to display policyholder details in a new window
    private void displayPolicyHolderDetails(PolicyHolder policyHolder) {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("PolicyHolder Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        Label codeLabel = new Label("PolicyHolder: " + "\n" +
                "CustomerId: " + policyHolder.getId() + "'\n" +
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
