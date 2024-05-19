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
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.example.asm2_insurance_claim_management_system.Providers.Manager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

public class CRUDManagerController implements CRUDoperation {
    // attributes for creating Surveyor
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldFullName;

    // Attributes of update()
    @FXML
    private TextField checkUpdateId;
    @FXML
    private TextField updatePassword;
    @FXML
    private TextField updateFullName;
    // Attributes of delete()
    @FXML
    private TextField deleteManager;
    // Attributes of view()
    @FXML
    private TextField viewManager;
    @FXML
    private Button viewManagerButton;

    @Override
    public boolean createEntity() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        String userName = textFieldId.getText();
        String password = textFieldPassword.getText();
        String fullName = textFieldFullName.getText();
        if (userName.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }

        List<Manager> managerList = session.createQuery("FROM Manager WHERE providerId = :managerId", Manager.class)
                .setParameter("managerId", userName)
                .getResultList();

        if (!managerList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Manager has been created.");
            return false;
        }

        Manager manager = new Manager();

        manager.setProviderId(userName);
        manager.setPassword(password);
        manager.setProviderName(fullName);

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            session.save(manager);

            // Commit the transaction
            session.getTransaction().commit();
            //Alert
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Create Successfully");

            //clear field
            textFieldId.clear();
            textFieldPassword.clear();
            textFieldFullName.clear();
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
            Manager manager = session.get(Manager.class, userName);
            List<Manager> managerList = session.createQuery("FROM Manager WHERE providerId = :managerId", Manager.class)
                    .setParameter("managerId", userName)
                    .getResultList();

            if (managerList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Manager does not exist.");
                return false;
            }

            // Update only non-empty fields
            if (!newPassword.isEmpty()) {
                manager.setPassword(newPassword);
            }
            if (!newFullName.isEmpty()) {
                manager.setProviderName(newFullName);
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
        String userName = deleteManager.getText();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();


        try {
            // Begin a transaction
            session.beginTransaction();

            // Load the entity you want to delete
            Manager manager = session.get(Manager.class, userName);

            // Check if the entity exists
            if (manager != null) {
                // Delete the entity
                session.delete(manager);
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Record deleted successfully.");


            } else {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "Manager with ID " + userName + " not found.");
            }

            // Commit the transaction
            session.getTransaction().commit();
            deleteManager.clear();
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
        String userName = viewManager.getText();

        try {

            // Begin a transaction
            session.beginTransaction();

            // Load the entity you want to delete
            Manager manager = session.get(Manager.class, userName);

            // Check if the entity exists
            if (manager != null) {
                // Load the Admin.fxml file
                // Create a new stage (window)
                displaySurveyorDetails(manager);
                viewManagerButton.getScene().getWindow().hide();


            } else {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "Manager with ID " + userName + " not found.");
            }

            // Commit the transaction
            session.getTransaction().commit();
            viewManager.clear();
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

    private void displaySurveyorDetails(Manager manager) {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Manager Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        Label codeLabel = new Label("Manager: " + "\n" +
                "ManagerId: " + manager.getProviderId() + "'\n" +
                "Password: " + manager.getPassword() + "'\n" +
                "FullName: " + manager.getProviderName()

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
