package org.example.asm2_insurance_claim_management_system.AdminGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Alert.ShowAlert;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

public class CRUDDependentController implements CRUDoperation {
    // attributes for creating Dependent
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldFullName;
    @FXML
    private TextField textFieldPolicyHolder;
    @FXML
    private TextField textFieldPolicyOwner;

    // Attributes of update()
    @FXML
    private TextField checkUpdateId;
    @FXML
    private TextField updatePassword;
    @FXML
    private TextField updateFullName;

    // attributes for deleting Dependent

    @FXML
    private TextField textFieldDeleteDependent;

    @Override
    public boolean createEntity() {
        String userName = textFieldId.getText();
        String password = textFieldPassword.getText();
        String fullName = textFieldFullName.getText();
        String policyHolderId = textFieldPolicyHolder.getText();
        String policyOwnerId = textFieldPolicyOwner.getText();
        if (userName.isEmpty() || password.isEmpty() || fullName.isEmpty() || policyOwnerId.isEmpty() || policyHolderId.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        PolicyHolder policyHolder = new PolicyHolder();
        List<PolicyHolder> policyHolderList = session.createQuery("FROM PolicyHolder ", PolicyHolder.class).getResultList();
        for (PolicyHolder testPolicyHolder : policyHolderList){
            if (testPolicyHolder.getId().equals(policyHolderId)){
                policyHolder = testPolicyHolder;
            }
        }
        PolicyOwner policyOwner = new PolicyOwner();
        List<PolicyOwner> policyOwnerListList = session.createQuery("FROM PolicyOwner ", PolicyOwner.class).getResultList();
        for (PolicyOwner testPolicyOwner : policyOwnerListList) {
            if (testPolicyOwner.getId().equals(policyOwnerId)) {
                policyOwner = testPolicyOwner;
            }
        }

        Dependent dependent = new Dependent();
        dependent.setCustomerId(userName);
        dependent.setPassword(password);
        dependent.setFullName(fullName);
        dependent.setPolicyHolder(policyHolder);
        dependent.setPolicyOwner(policyOwner);


        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            session.save(dependent);// or session.persist(policyHolder)

            // Commit the transaction
            session.getTransaction().commit();
            //Alert
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION,"Successful","Create Successfully");

            //clear field
            textFieldId.clear();
            textFieldPassword.clear();
            textFieldFullName.clear();
            textFieldPolicyOwner.clear();
            textFieldPolicyOwner.clear();

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

            // Retrieve the entity to update
            Dependent dependent = session.get(Dependent.class, userName);
            if (dependent == null) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR,"ERROR","dependent not found");
                return false;
            }
            // Update only non-empty fields
            if (!newPassword.isEmpty()) {
                dependent.setPassword(newPassword);
            }
            if (!newFullName.isEmpty()) {
                dependent.setFullName(newFullName);
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

        String userName = textFieldDeleteDependent.getText();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();


        try {
            // Begin a transaction
            session.beginTransaction();

            // Load the entity you want to delete
            Dependent dependent = session.get(Dependent.class, userName);

            // Check if the entity exists
            if (dependent != null) {
                // Delete the entity
                session.delete(dependent);
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.INFORMATION,"Successful","Record deleted successfully.");


            } else {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR,"ERROR","Record with ID " + userName + " not found.");
            }

            // Commit the transaction
            session.getTransaction().commit();
            textFieldDeleteDependent.clear();
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
        return false;
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
