package org.example.asm2_insurance_claim_management_system.PolicyHolderGUI;

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
import org.example.asm2_insurance_claim_management_system.Claim.BankInfo;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Claim.Status;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Interface.SuperCustomer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class PolicyHolderClaimController implements SuperCustomer {
    private String fileData; // This will store the encoded PDF file data
    //Atrributes for Flie Claim
    @FXML
    private TextField textFieldClaimID;
    @FXML
    private TextField textFieldClaimAmount;
    @FXML
    private TextField newBankIdText;
    @FXML
    private TextField newBankNameText;
    @FXML
    private TextField newBankHolderText;
    @FXML
    private TextField newAccNumText;


    //Attribute for Update Claim
    @FXML
    private TextField textFieldUpdateClaimID;
    @FXML
    private TextField textFieldUpdateClaimAmount;

    @FXML
    private TextField BankNameText;
    @FXML
    private TextField BankHolderText;
    @FXML
    private TextField AccNumText;

    // Attribute for Update policyHolder Information
    @FXML
    private TextField textFieldUpdatePolicyHolderName;
    @FXML
    private TextField textFieldUpdatePolicyHolderPassword;

    // Attributes for FileClaimforDependent()
    @FXML
    private TextField textDependentID;
    @FXML
    private TextField textFieldClaimDependentID;
    @FXML
    private TextField textFieldClaimAmountDependent;
    @FXML
    private TextField newBankIdTextDependent;
    @FXML
    private TextField newBankNameTextDependent;
    @FXML
    private TextField newBankHolderTextDependent;
    @FXML
    private TextField newAccNumTextDependent;
    @FXML
    private Button uploadPDF = new Button();

    // Attributes for UpdateClaimForDependent
    @FXML
    private TextField textCheckDependentID;
    @FXML
    private TextField textFieldCheckClaimDependentID;
    @FXML
    private TextField textUpdateFieldClaimAmountDependent;
    @FXML
    private TextField updateBankNameTextDependent;
    @FXML
    private TextField updateBankHolderTextDependent;
    @FXML
    private TextField updateAccNumTextDependent;


    //Attributes for retrieveClaimForDependent
    @FXML
    private TextField retrieveClaimForDependent;
    @FXML
    private Button viewDependentClaimsButton;

    // Attributes for updateInfoForDependent()
    @FXML
    private TextField checkDependentId;
    @FXML
    private TextField updateDependentName;
    @FXML
    private TextField updateDependentPassword;


    // Attributes for getAll Dependent
    @FXML
    private Button viewAllDependentButton;
    private PolicyHolder policyHolder;

    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    @Override
    public boolean filePolicyHolderClaim() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        String claimID = textFieldClaimID.getText();
        double claimAmount = Double.parseDouble(textFieldClaimAmount.getText());
        String bankID = newBankIdText.getText();
        String bankName = newBankNameText.getText();
        String ownerName = newBankHolderText.getText();
        String accountNumber = newAccNumText.getText();

        if (claimID.isEmpty() || textFieldClaimAmount.getText().isEmpty() || bankID.isEmpty() || bankName.isEmpty() || ownerName.isEmpty() || accountNumber.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }

        List<Claim> claimList = session.createQuery("FROM Claim WHERE claimId = :claimID", Claim.class)
                .setParameter("claimID", claimID)
                .getResultList();

        if (!claimList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Claim has been existed.");
            return false;
        }

        List<BankInfo> bankInfoList = session.createQuery("FROM BankInfo WHERE bankID = :bankID", BankInfo.class)
                .setParameter("bankID", bankID)
                .getResultList();

        if (!bankInfoList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Bank ID has been existed.");
            return false;
        }
        BankInfo bank = new BankInfo();
        bank.setBankID(bankID);
        bank.setBankName(bankName);
        bank.setAccountNumber(accountNumber);
        bank.setOwnerName(ownerName);
        Claim claim = new Claim();
        claim.setClaimId(claimID);
        claim.setClaimDate(LocalDate.now());
        claim.setListOfDocument(fileData);
        claim.setStatus(Status.NEW);
        claim.setInsuranceCard(policyHolder.getInsuranceCard());
        claim.setPolicyHolder(policyHolder);
        claim.setClaimAmount(claimAmount);
        claim.setBankInfo(bank);
        // List of document

        try {
            session.beginTransaction();
            session.save(claim);
            ShowAlert successfulAlert = new ShowAlert();
            successfulAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "File Claim Successfully");
            session.getTransaction().commit();
            textFieldClaimID.clear();
            textFieldClaimAmount.clear();
            newBankIdText.clear();
            newBankNameText.clear();
            newBankHolderText.clear();
            newAccNumText.clear();
            return true;
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while filing the claim.");
        } finally {
            // Close the session and session factory
//            session.close();
//            sessionFactory.close();
            if (session != null) {
                session.close();
            }
        }
        return false;

    }

    @Override
    public boolean updatePolicyHolderClaim() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;
        try {
            session = sessionFactory.openSession();

            String claimID = textFieldUpdateClaimID.getText();
            Double claimAmount = null;
            claimAmount = Double.parseDouble(textFieldUpdateClaimAmount.getText());
            String bankName = BankNameText.getText();
            String ownerName = BankHolderText.getText();
            String accountNumber = AccNumText.getText();
            // List of document

            session.beginTransaction();
            String desiredClaim = "SELECT c FROM Claim c JOIN c.policyHolder h WHERE h.id = :policyHolderId AND c.dependent IS NULL AND c.id = :claimId";
            List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                    .setParameter("policyHolderId", policyHolder.getId()).setParameter("claimId", claimID)
                    .getResultList();

            if (claimList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Claim does not exist.");
                return false;
            }

            Claim claim = claimList.get(0);
            if (!textFieldUpdateClaimAmount.getText().isEmpty()) {
                claim.setClaimAmount(claimAmount);
            }
            if (!bankName.isEmpty()) {
                claim.getBankInfo().setBankName(bankName);
            }
            if (!ownerName.isEmpty()) {
                claim.getBankInfo().setOwnerName(ownerName);
            }
            if (!accountNumber.isEmpty()) {
                claim.getBankInfo().setAccountNumber(accountNumber);
            }

            session.getTransaction().commit();
            ShowAlert successfulAlert = new ShowAlert();
            successfulAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Update Claim Successfully");
            textFieldUpdateClaimID.clear();
            textFieldUpdateClaimAmount.clear();
            BankNameText.clear();
            BankHolderText.clear();
            AccNumText.clear();
            return true;
            // Update successful

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Something wrong.");
            }
            ex.printStackTrace(); // Print error details
        } finally {
            // Close the session and session factory
            if (session != null) {
                session.close();
            }

        }
        return false;
    }

    @Override
    public boolean updatePolicyHolderInfo() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        String newPolicyHolderName = textFieldUpdatePolicyHolderName.getText();
        String newPassword = textFieldUpdatePolicyHolderPassword.getText();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            PolicyHolder policyHolderToUpdate = session.get(PolicyHolder.class, policyHolder.getId());
            if (!newPolicyHolderName.isEmpty()) {
                policyHolderToUpdate.setFullName(newPolicyHolderName);
            }
            if (!newPassword.isEmpty()) {
                policyHolderToUpdate.setPassword(newPassword);
            }
            textFieldUpdatePolicyHolderName.clear();
            textFieldUpdatePolicyHolderPassword.clear();
            session.getTransaction().commit();

            ShowAlert successfulAlert = new ShowAlert();
            successfulAlert.showAlert(Alert.AlertType.CONFIRMATION, "Confirm", "Are you sure you want to update the policy holder information?");
            return true; // Update successful

        } catch (Exception ex) {
            ex.printStackTrace(); // Print error details
            return false; // Update failed
        }
    }


    @Override
    public boolean retrievePolicyHolderClaim() {
        return false;
    }

    @Override
    public boolean showPolicyHolderInfo() {
        return false;
    }

    @FXML
    public void initialize() {
        uploadPDF.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setTitle("Select PDF File");
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                try {
                    fileData = encodeFileToBase64(selectedFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ShowAlert errorAlert = new ShowAlert();
                    errorAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to encode PDF file.");
                }
            } else {
                ShowAlert errorAlert = new ShowAlert();
                errorAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "No file to selected.");
            }
        });
    }

    @Override
    public boolean fileClaimForDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();


        try {
            String dependentId = textDependentID.getText();
            String claimID = textFieldClaimDependentID.getText();
            double claimAmount = Double.parseDouble(textFieldClaimAmountDependent.getText());
            String bankID = newBankIdTextDependent.getText();
            String bankName = newBankNameTextDependent.getText();
            String ownerName = newBankHolderTextDependent.getText();
            String accountNumber = newAccNumTextDependent.getText();

            if (dependentId.isEmpty() || claimID.isEmpty() || bankID.isEmpty() || bankName.isEmpty() || ownerName.isEmpty() || accountNumber.isEmpty()) {
                // If any required field is empty, show an alert message
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return false; // Abort the create operation
            }

            try {
                session.beginTransaction();
                // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
                String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId AND d.id = :dependentID";
                List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                        .setParameter("policyHolderId", policyHolder.getId())
                        .setParameter("dependentID", dependentId)
                        .getResultList();

                if (dependentList.isEmpty()) {
                    ShowAlert showAlert = new ShowAlert();
                    showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist.");
                    return false;
                }
                Dependent dependent = dependentList.get(0);

                List<Claim> claimList = session.createQuery("FROM Claim WHERE claimId = :claimID", Claim.class)
                        .setParameter("claimID", claimID)
                        .getResultList();

                if (!claimList.isEmpty()) {
                    ShowAlert showAlert = new ShowAlert();
                    showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Claim has been existed.");
                    return false;
                }

                List<BankInfo> bankInfoList = session.createQuery("FROM BankInfo WHERE bankID = :bankID", BankInfo.class)
                        .setParameter("bankID", bankID)
                        .getResultList();

                if (!bankInfoList.isEmpty()) {
                    ShowAlert showAlert = new ShowAlert();
                    showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Bank ID has been existed.");
                    return false;
                }
                BankInfo bankInfo = new BankInfo();
                bankInfo.setBankID(bankID);
                bankInfo.setBankName(bankName);
                bankInfo.setOwnerName(ownerName);
                bankInfo.setAccountNumber(accountNumber);
                Claim claim = new Claim();
                claim.setClaimId(claimID);
                claim.setClaimDate(LocalDate.now());
                claim.setStatus(Status.NEW);
                claim.setListOfDocument(fileData);
                claim.setInsuranceCard(dependent.getPolicyHolder().getInsuranceCard());
                claim.setPolicyHolder(dependent.getPolicyHolder());
                claim.setClaimAmount(claimAmount);
                claim.setDependent(dependent);
                claim.setBankInfo(bankInfo);

                // Save the bankInfo and claim
                session.save(bankInfo);
                session.save(claim);

                session.getTransaction().commit();

                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Create Successfully");
                return true;


            } catch (Exception ex) {
                session.getTransaction().rollback();
                ex.printStackTrace();
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while filing the claim.");
                return false;
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        } catch (NumberFormatException e) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number for the claim amount.");
            return false;
        }
    }

    @Override
    public boolean updateClaimForDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            String dependentId = textCheckDependentID.getText();
            String claimID = textFieldCheckClaimDependentID.getText();
            double claimAmount = Double.parseDouble(textUpdateFieldClaimAmountDependent.getText());
            String bankName = updateBankNameTextDependent.getText();
            String ownerName = updateBankHolderTextDependent.getText();
            String accountNumber = updateAccNumTextDependent.getText();

            if (dependentId.isEmpty()) {
                // If any required field is empty, show an alert message
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return false; // Abort the create operation
            }

            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId AND d.id = :dependentID";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyHolderId", policyHolder.getId())
                    .setParameter("dependentID", dependentId)
                    .getResultList();

            if (dependentList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist.");
                return false;
            }

            try {
                session.beginTransaction();
                String desiredClaim = "SELECT c FROM Claim c JOIN c.dependent d WHERE d.id = :dependentId AND c.id = :claimId";
                List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                        .setParameter("dependentId", dependentId)
                        .setParameter("claimId", claimID)
                        .getResultList();

                if (claimList.isEmpty()) {
                    ShowAlert showAlert = new ShowAlert();
                    showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Claim does not exist.");
                    return false;
                }

                Claim claim = claimList.get(0);
                if (!textUpdateFieldClaimAmountDependent.getText().isEmpty()) {
                    claim.setClaimAmount(claimAmount);
                }

                BankInfo bankInfo = claim.getBankInfo();
                if (!bankName.isEmpty()) {
                    bankInfo.setBankName(bankName);
                }
                if (!ownerName.isEmpty()) {
                    bankInfo.setOwnerName(ownerName);
                }
                if (!accountNumber.isEmpty()) {
                    bankInfo.setAccountNumber(accountNumber);
                }

                session.getTransaction().commit();
                ShowAlert successfulAlert = new ShowAlert();
                successfulAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Create Claim Successfully");
                textCheckDependentID.clear();
                textFieldCheckClaimDependentID.clear();
                textUpdateFieldClaimAmountDependent.clear();
                updateBankNameTextDependent.clear();
                updateBankHolderTextDependent.clear();
                updateAccNumTextDependent.clear();
                return true; // Update successful

            } catch (Exception ex) {
                // Rollback the transaction in case of an exception
                session.getTransaction().rollback();
                ex.printStackTrace();
                ShowAlert successfulAlert = new ShowAlert();
                successfulAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "Something Wrong ");
            } finally {
                // Close the session and session factory
//            session.close();
//            sessionFactory.close();
                if (session != null) {
                    session.close();
                }
            }
        } catch (NumberFormatException e) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number for the claim amount.");
            return false;
        }

        return false;
    }


    @Override
    public boolean retrieveClaimForDependent() {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Dependent Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        codeContainer.setPadding(new Insets(10));
        codeContainer.setSpacing(10);

        String dependentId = retrieveClaimForDependent.getText();
        if (dependentId.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId AND d.id = :dependentID";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyHolderId", policyHolder.getId()).setParameter("dependentID", dependentId)
                .getResultList();

        if (!dependentList.isEmpty()) {
            try {
                session.beginTransaction();
                String desiredClaim = "SELECT c FROM Claim c JOIN c.policyHolder h WHERE h.id = :policyHolderId AND c.dependent IS NOT NULL";
                List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                        .setParameter("policyHolderId", policyHolder.getId()).getResultList();
                for (Claim claim : claimList) {


                    if (dependentList.get(0).getId().equals(claim.getDependent().getId())) {
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
            } catch (Exception ex) {
                // Rollback the transaction in case of an exception
                session.getTransaction().rollback();
                ex.printStackTrace();
            } finally {
                // Close the session and session factory
//            sessionFactory.close();
                if (session != null) {
                    session.close();
                }
            }
            // Create a scene with the code container
            Button returnButton = new Button("Return");
            returnButton.setOnAction(this::goBack);

            // Add the Close button to the VBox
            codeContainer.getChildren().add(returnButton);
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(codeContainer);
            scrollPane.setFitToWidth(true);
            Scene codeScene = new Scene(scrollPane, 400, 600);
            codeStage.setScene(codeScene);
            codeStage.show();
        } else {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist");
            return false;
        }

        return false;
    }

    @Override
    public boolean updateInfoForDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();


        String dependentId = checkDependentId.getText();
        String newDependentName = updateDependentName.getText();
        String newPassword = updateDependentPassword.getText();

        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId AND d.id = :dependentID";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyHolderId", policyHolder.getId())
                .setParameter("dependentID", dependentId)
                .getResultList();

        if (dependentList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist.");
            return false;
        }
        Dependent dependent = dependentList.get(0);
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            dependent = session.get(Dependent.class, dependentId);
            dependent.setFullName(newDependentName);
            dependent.setPassword(newPassword);

            session.getTransaction().commit();
            ShowAlert successfulAlert = new ShowAlert();
            successfulAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Update Successfully");
            checkDependentId.clear();
            updateDependentPassword.clear();
            updateDependentName.clear();
            return true; // Update successful
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
//            session.close();
//            sessionFactory.close();
            if (session != null) {
                session.close();
            }

        }

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
            sessionFactory.close();
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

        // Create a scene with the code container
        Scene codeScene = new Scene(codeContainer, 400, 300);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
        viewAllDependentButton.getScene().getWindow().hide();
    }

    @FXML
    private void goBack(ActionEvent event) {
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

    public static String encodeFileToBase64(File file) throws IOException {
        byte[] fileContent = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileContent);
        }
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static byte[] decodeBase64ToFile(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

}
