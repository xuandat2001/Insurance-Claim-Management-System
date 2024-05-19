package org.example.asm2_insurance_claim_management_system.AdminGUI;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Alert.ShowAlert;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;


public class CRUDPolicyOwnerController implements CRUDoperation {
    // Attributes for creating PolicyOwner
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldFullName;
    @FXML
    private TextField textLocation;
    @FXML
    private TextField textInsuranceFee;
    // Attributes of update()
    @FXML
    private TextField checkUpdateId;
    @FXML
    private TextField updatePassword;
    @FXML
    private TextField updateFullName;
    @FXML
    private TextField updateLocation;
    @FXML
    private TextField updateInsuranceFee;

    // Attributes of delete()
    @FXML
    private TextField deletePolicyOwner;
    // Attributes of view()
    @FXML
    private TextField viewPolicyOwner;
    @FXML
    private Button viewPolicyOwnerButton;
    @FXML
    @Override
    public boolean createEntity() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        String userName = textFieldId.getText();
        String password = textFieldPassword.getText();
        String fullName = textFieldFullName.getText();
        String location = textLocation.getText();
        String insuranceFee = textInsuranceFee.getText();

        if (userName.isEmpty() || password.isEmpty() || fullName.isEmpty() || location.isEmpty() || insuranceFee.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }

        String desiredPolicyOwner= "SELECT o FROM PolicyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyOwner> policyOwnerList = session.createQuery(desiredPolicyOwner, PolicyOwner.class)
                .setParameter("policyOwnerId", userName)
                .getResultList();

        if (!policyOwnerList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Owner has been created");
            return false;
        }

        PolicyOwner policyOwner = new PolicyOwner();
        policyOwner.setCustomerId(userName);
        policyOwner.setPassword(password);
        policyOwner.setFullName(fullName);
        policyOwner.setLocation(location);
        policyOwner.setInsuranceFee(Double.valueOf(insuranceFee) );
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            session.save(policyOwner);// or session.persist(policyHolder)

            // Commit the transaction
            session.getTransaction().commit();
            //Alert
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION,"Successful","Create Successfully");

            //clear field
            textFieldId.clear();
            textFieldPassword.clear();
            textFieldFullName.clear();
            textLocation.clear();
            textInsuranceFee.clear();
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
        String newLocation = updateLocation.getText();
        String newInsuranceFee = updateInsuranceFee.getText();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            String desiredPolicyOwner= "SELECT o FROM PolicyOwner o WHERE o.id = :policyOwnerId";
            List<PolicyOwner> policyOwnerList = session.createQuery(desiredPolicyOwner, PolicyOwner.class)
                    .setParameter("policyOwnerId", userName)
                    .getResultList();

            if (policyOwnerList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist");
                return false;
            }
            PolicyOwner policyOwner = policyOwnerList.get(0);
            // Update only non-empty fields
            if (!newPassword.isEmpty()) {
                policyOwner.setPassword(newPassword);
            }
            if (!newFullName.isEmpty()) {
                policyOwner.setFullName(newFullName);
            }

            if (!newLocation.isEmpty()) {
                policyOwner.setLocation(newLocation);
            }
            if (!newInsuranceFee.isEmpty()) {
                policyOwner.setLocation(newLocation);
            }
            // Commit the transaction
            session.getTransaction().commit();

            // alert
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION,"Successful","Update Successfully");
            //clear the field
            checkUpdateId.clear();
            updatePassword.clear();
            updateFullName.clear();
            updateLocation.clear();
            updateInsuranceFee.clear();
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
        String userName = deletePolicyOwner.getText();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();


        try {
            // Begin a transaction
            session.beginTransaction();

            // Load the entity you want to delete
            PolicyOwner policyOwner = session.get(PolicyOwner.class, userName);

            // Check if the entity exists
            if (policyOwner != null) {
                // Delete the entity
                session.delete(policyOwner);
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.INFORMATION,"Successful","Record deleted successfully.");


            } else {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR,"ERROR","Policy Owner with ID " + userName + " not found.");
            }

            // Commit the transaction
            session.getTransaction().commit();
            deletePolicyOwner.clear();
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
        String userName = viewPolicyOwner.getText();

        try {

            // Begin a transaction
            session.beginTransaction();

            // Load the entity you want to delete
            PolicyOwner policyOwner = session.get(PolicyOwner.class, userName);

            // Check if the entity exists
            if (policyOwner != null) {
                // Load the Admin.fxml file
                // Create a new stage (window)
                displayPolicyOwnerDetails(policyOwner);
                viewPolicyOwnerButton.getScene().getWindow().hide();


            } else {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR,"ERROR","Policy Owner with ID " + userName + " not found.");
            }

            // Commit the transaction
            session.getTransaction().commit();
            viewPolicyOwner.clear();
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
    private void displayPolicyOwnerDetails(PolicyOwner policyOwner) {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("PolicyHolder Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        Label codeLabel = new Label("PolicyHolder: " + "\n" +
                "CustomerId: " + policyOwner.getId() + "'\n" +
                "Password: " + policyOwner.getPassword() + "'\n" +
                "FullName: " + policyOwner.getFullName() + "'\n" +
                "Location: " + policyOwner.getLocation()
        );

        // Add the code label to the VBox
        codeContainer.getChildren().add(codeLabel);
        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBack);
        // Add the Close button to the VBox
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
