package org.example.asm2_insurance_claim_management_system.PolicyOwnerGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;
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
    private static final double DEPENDENT_FEE = 0.6;

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
            adminStage.setScene(new Scene(policyOwnerRoot, 500, 750));

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
        createScenePolicyOwner (url, retrieveClaimOfPolicyHolder);
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
        createScenePolicyOwner (url, retrieveClaimOfDependent);
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
    protected void onShowInfoPolicyHolder() {
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .getResultList();

            // Commit the transaction
            session.getTransaction().commit();
            retrievePolicyHolderDetails(policyHolderList);
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();
        }
    }

    private void retrievePolicyHolderDetails(List<PolicyHolder> policyHolderList) {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("PolicyHolder Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        codeContainer.setPadding(new Insets(10));
        codeContainer.setSpacing(10);

        for (PolicyHolder policyHolder : policyHolderList) {
            Label codeLabel = new Label(
                    "PolicyHolder ID: " + policyHolder.getId() + "\n" +
                            "Full Name: " + policyHolder.getFullName() + "\n" +
                            "Password: " + policyHolder.getPassword()
            );
            codeContainer.getChildren().add(codeLabel);
        }

        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBackMainMenu);
        codeContainer.getChildren().add(returnButton);

        // Create a ScrollPane and set the VBox as its content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(codeContainer);
        scrollPane.setFitToWidth(true);
        //scrollPane.setFitToHeight(true); // Enable vertical scrolling

        // Set minimum sizes to ensure visibility


        // Create a scene with the scroll pane
        Scene codeScene = new Scene(scrollPane, 400, 300);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
        retrieveInfoOfPolicyHolder.getScene().getWindow().hide();
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
    protected void onGetAllDependent() {
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            // Assuming policyHolderId is the ID of the PolicyOwner you want to retrieve dependents for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .getResultList();
            retrieveDependentDetails(dependentList);
            // Commit the transaction
            session.getTransaction().commit();

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();

        }


    }
    private void retrieveDependentDetails(List<Dependent> dependentList) {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Dependent Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        codeContainer.setPadding(new Insets(10));
        codeContainer.setSpacing(10);

        for (Dependent dependent : dependentList) {
            Label codeLabel = new Label(
                    "Dependent ID: " + dependent.getId() + "\n" +
                            "FullName: " + dependent.getFullName() + "\n" +
                            "Password: " + dependent.getPassword()
            );
            codeContainer.getChildren().add(codeLabel);


        }
        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBackMainMenu);
        // Add the Close button to the VBox
        codeContainer.getChildren().add(returnButton);
        // Create a ScrollPane and set the VBox as its content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(codeContainer);
        scrollPane.setFitToWidth(true);
        // Create a scene with the code container
        Scene codeScene = new Scene(scrollPane, 400, 300);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
        retrieveInfoOfDependent.getScene().getWindow().hide();
    }
    public int getNumberOfPolicyHolder() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .getResultList();

            int count = policyHolderList.size();
            return count;
//            System.out.println(count);

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();
        }

        return 0;
    }
    public int getNumberOfDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Perform a query
// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .getResultList();

            int count = dependentList.size();
            return count;
//            System.out.println(count);

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();
        }
        return 0;
    }
    public double onCalcInsuranceFee() {
        double policyHolderFee = this.getNumberOfPolicyHolder() * policyOwner.getInsuranceFee();
        double dependentFee = this.getNumberOfDependent() * policyOwner.getInsuranceFee() * DEPENDENT_FEE;
        double totalInsuranceFee = policyHolderFee + dependentFee;
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Insurance Fee yearly");
        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        codeContainer.setPadding(new Insets(10));
        codeContainer.setSpacing(10);
        Label codeLabel = new Label(
                "Total Insurance Fee have to pay yearly: " + totalInsuranceFee
        );
        codeContainer.getChildren().add(codeLabel);

        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBackMainMenu);
        // Add the Close button to the VBox
        codeContainer.getChildren().add(returnButton);
        // Create a scene with the code container
        Scene codeScene = new Scene(codeContainer, 400, 300);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
        calcInsuranceFee.getScene().getWindow().hide();
        return 0;
    }
    @FXML
    private void goBackMainMenu(ActionEvent event) {
        try {
            // Initialize the FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/asm2_insurance_claim_management_system/PolicyOwner/PolicyOwner.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller
            PolicyOwnerController controller = loader.getController();
            controller.setPolicyOwner(policyOwner);

            // Set the necessary data in the controller if needed
            // Example: controller.setPolicyHolder(policyHolder);

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
    private void goBackLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
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



