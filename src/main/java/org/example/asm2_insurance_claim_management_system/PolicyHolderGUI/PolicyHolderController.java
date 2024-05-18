package org.example.asm2_insurance_claim_management_system.PolicyHolderGUI;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Interface.SuperCustomer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.io.IOException;
import java.util.List;

public class PolicyHolderController implements SuperCustomer {
    private PolicyHolder policyHolder;

    // Attributes for ShowInfoPolicyHolder() method
    @FXML
    private Button viewPolicyHolderButton;

    // Attributes for retrievePolicyHolderClaim() method
    @FXML
    private Button viewPolicyHolderClaimsButton;

// Attributes for filePolicyHolderClaim() method
    @FXML
    private Button createFromClaim;


// Attributes for updatePolicyHolderInfo()
    @FXML
    private TextField newPasswordText;
    @FXML
    private TextField newFullNameText;
    @FXML
    private Button createFromUpdateClaim;
    @FXML
    private Button createFromUpdate;


// Attributes for createPolicyHolderClaim()

    @FXML
    private TextField newClaimIdText;
    @FXML
    private TextField newClaimAmountText;
    @FXML
    private TextField newBankIdText;
    @FXML
    private TextField newBankNameText;
    @FXML
    private TextField newBankHolderText;
    @FXML
    private TextField newAccNumText;
// Attribute for getAllDependent()
    @FXML
    private Button viewAllDependentButton;
    @FXML
    private Button createFromFileClaimForDependent;
    @FXML
    private Button updateFromFileClaimForDependent;
    @FXML
    private Button retrieveFromFileClaimForDependent;
    @FXML
    private Button updateDependent;

    @FXML
    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    // Methods for filePolicyHolderClaim()
    public  void createPolicyHolderClaimForm(String url, String titlePage, Button button ){


        try {
            // Load the CreateClaim.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));

            // Load the root element (in this case, VBox)
            VBox root = loader.load();

            // Get the controller associated with CreateClaim.fxml
            PolicyHolderClaimController policyHolderClaimController = loader.getController();

            // Pass necessary data or references to the controller, if needed
            policyHolderClaimController.setPolicyHolder(policyHolder);

            // Create a new stage for the Create Claim UI
            Stage createClaimStage = new Stage();
            createClaimStage.setTitle(titlePage);
            createClaimStage.setScene(new Scene(root, 600, 400));

            // Show the Create Claim UI stage
            createClaimStage.show();

            // Close the current stage (the one containing the button)
            button.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void createFilePolicyHolderClaimForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyHolder/createClaim.fxml";
        createPolicyHolderClaimForm(url,"Create Claim", createFromClaim);
    }
    public  void createUpdatePolicyHolderClaimForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyHolder/updateClaim.fxml";
        createPolicyHolderClaimForm(url,"Update Claim", createFromUpdateClaim);
    }
    public  void createUpdatePolicyHolderForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyHolder/updatePolicyHolder.fxml";
        createPolicyHolderClaimForm(url,"Update Claim", createFromUpdate);
    }
    public  void fileClaimForDependentForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyHolder/fileClaimForDependent.fxml";
        createPolicyHolderClaimForm(url,"File Claim For Dependent", createFromFileClaimForDependent);
    }
    public  void updateClaimForDependentForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyHolder/updateClaimForDependent.fxml";
        createPolicyHolderClaimForm(url,"Update Claim For Dependent", updateFromFileClaimForDependent);
    }
    public  void retrieveClaimForDependentForm(){
        try {
            // Load the CreateClaim.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/asm2_insurance_claim_management_system/PolicyHolder/retrieveClaimForDependent.fxml"));

            // Load the root element (in this case, VBox)
            ScrollPane root = loader.load();

            // Get the controller associated with CreateClaim.fxml
            PolicyHolderClaimController policyHolderClaimController = loader.getController();

            // Pass necessary data or references to the controller, if needed
            policyHolderClaimController.setPolicyHolder(policyHolder);

            // Create a new stage for the Create Claim UI
            Stage createClaimStage = new Stage();
            createClaimStage.setTitle("Get Claim For Dependent");
            createClaimStage.setScene(new Scene(root, 600, 400));

            // Show the Create Claim UI stage
            createClaimStage.show();

            // Close the current stage (the one containing the button)
            retrieveFromFileClaimForDependent.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void updateForDependentForm(){
        String url = "/org/example/asm2_insurance_claim_management_system/PolicyHolder/updateDependentInfo.fxml";
        createPolicyHolderClaimForm(url,"Update Dependent", updateDependent);
    }






    @Override
    public boolean filePolicyHolderClaim() {
        return false;
    }

    @Override
    public boolean updatePolicyHolderClaim() {
        return false;
    }




// retrievePolicyHolderClaim()
    @Override
    public boolean retrievePolicyHolderClaim() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            String desiredClaim = "SELECT c FROM Claim c WHERE c.dependent IS NULL";
            List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                    .getResultList();
            retrievePolicyHolderDetailsClaim(claimList);
            session.getTransaction().commit();
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
    private void retrievePolicyHolderDetailsClaim(List<Claim> claimList) {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("PolicyHolder Details");

        // Create a VBox to hold the content
        VBox contentBox = new VBox();
        contentBox.setPadding(new Insets(10));
        contentBox.setSpacing(10);

        // Add claim details to the VBox
        for (Claim claim : claimList) {
            if (policyHolder.getId().equals(claim.getPolicyHolder().getId())) {
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
                contentBox.getChildren().add(codeLabel);
            }
        }

        // Add the Return button to the VBox
        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBackMainMenu);
        contentBox.getChildren().add(returnButton);

        // Create a ScrollPane and set the VBox as its content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contentBox);
        scrollPane.setFitToWidth(true);

        // Create a scene with the ScrollPane
        Scene codeScene = new Scene(scrollPane, 400, 300);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
        viewPolicyHolderClaimsButton.getScene().getWindow().hide();
    }




    //updatePolicyHolderInfo()

    @Override
    public boolean updatePolicyHolderInfo() {
      return  false;
    }

    @Override
    public boolean showPolicyHolderInfo() {
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
        }

        return false;
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
        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBackMainMenu);
        // Add the Close button to the VBox
        codeContainer.getChildren().add(returnButton);

        // Create a ScrollPane and set the VBox as its content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(codeContainer);
        scrollPane.setFitToWidth(true);
        // Set the scene of the new stage with the VBox
        codeStage.setScene(new Scene(scrollPane, 400, 200));

        // Show the new stage
        codeStage.show();
    }

    //public boolean showPolicyHolderInfo()

    @Override
    public boolean fileClaimForDependent() {
        return false;
    }

    @Override
    public boolean updateClaimForDependent() {
        return false;
    }

    @Override
    public boolean retrieveClaimForDependent() {
        return false;
    }

    @Override
    public boolean updateInfoForDependent() {
        return false;
    }

    @Override
    public boolean getAllDependent() {
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            // Assuming policyHolderId is the ID of the PolicyHolder you want to retrieve dependents for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyHolderId", policyHolder.getId())
                    .getResultList();
          retrieveDependentDetails(dependentList);
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
                                "Claim Date: " + dependent.getFullName() + "\n" +
                                "Claim Amount: " + dependent.getPassword()
                );
                codeContainer.getChildren().add(codeLabel);


        }
        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBackMainMenu);
        // Add the Close button to the VBox
        codeContainer.getChildren().add(returnButton);
        // Create a scene with the code container
        Scene codeScene = new Scene(codeContainer, 400, 300);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
        viewAllDependentButton.getScene().getWindow().hide();
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
    @FXML
    private void goBackMainMenu(ActionEvent event) {
        try {
            // Initialize the FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/asm2_insurance_claim_management_system/PolicyHolder/PolicyHolder.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller
            PolicyHolderController controller = loader.getController();
            controller.setPolicyHolder(policyHolder);

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

}
