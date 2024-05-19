package org.example.asm2_insurance_claim_management_system.PolicyOwnerGUI;

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
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.SuperCustomer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

public class CRUDForPolicyOwner extends PolicyOwner implements SuperCustomer {
    private String fileData; // This will store the encoded PDF file data
    private PolicyOwner policyOwner;
    @FXML
    private TextField textFieldPolicyHolderID;
    @FXML
    private TextField textFieldDependentID;
    @FXML
    private TextField textFieldClaimID;
    @FXML
    private TextField textFieldClaimAmount;
    @FXML
    private TextField textFieldBankID;
    @FXML
    private TextField textFieldBankName;
    @FXML
    private TextField textFieldOwnerName;
    @FXML
    private TextField textFieldAccountNumber;
    @FXML
    private TextField textFieldDocument;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldFullName;
    @FXML
    private TextField textFieldExpirationDate;
    @FXML
    private TextField textFieldCardNumber;
    @FXML
    private Button uploadPDF = new Button();

    public void setPolicyOwner(PolicyOwner policyOwner) {
        this.policyOwner = policyOwner;
    }

    @FXML
    private Button saveClaimButton;

    @Override
    public boolean filePolicyHolderClaim() {
        try {
            String policyHolderId = textFieldPolicyHolderID.getText();
            String claimID = textFieldClaimID.getText();
            double claimAmount = Double.parseDouble(textFieldClaimAmount.getText());
            String bankID = textFieldBankID.getText();
            String bankName = textFieldBankName.getText();
            String ownerName = textFieldOwnerName.getText();
            String accountNumber = textFieldAccountNumber.getText();

            if (policyHolderId.isEmpty() || claimID.isEmpty() || bankID.isEmpty() || bankName.isEmpty() || ownerName.isEmpty() || accountNumber.isEmpty()) {
                // If any required field is empty, show an alert message
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return false; // Abort the create operation
            }

            SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
            Session session = sessionFactory.openSession();

            try {
                session.beginTransaction();
                // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
                String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId AND h.id = :policyHolderID";
                List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                        .setParameter("policyOwnerId", policyOwner.getId())
                        .setParameter("policyHolderID", policyHolderId)
                        .getResultList();

                if (policyHolderList.isEmpty()) {
                    ShowAlert showAlert = new ShowAlert();
                    showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist.");
                    return false;
                }
                PolicyHolder policyHolder = policyHolderList.get(0);

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
                claim.setInsuranceCard(policyHolder.getInsuranceCard());
                claim.setPolicyHolder(policyHolder);
                claim.setClaimAmount(claimAmount);
                claim.setDependent(null);
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
    public boolean retrievePolicyHolderClaim() {

        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("PolicyHolder Claim Details");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        codeContainer.setPadding(new Insets(10));
        codeContainer.setSpacing(10);

        String policyHolderId = textFieldPolicyHolderID.getText();

        if (policyHolderId.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId AND h.id = :policyHolderID";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwner.getId()).setParameter("policyHolderID", policyHolderId)
                .getResultList();

        if (!policyHolderList.isEmpty()) {
            try {
                session.beginTransaction();
                String desiredClaim = "SELECT c FROM Claim c WHERE c.policyHolder IS NOT NULL AND c.dependent IS NULL";
                List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                        .getResultList();
                for (Claim claim : claimList) {
                    if (policyHolderList.get(0).getId().equals(claim.getPolicyHolder().getId())) {
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
                        codeContainer.getChildren().add(codeLabel);
                    }
                }
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
            Button returnButton = new Button("Return");
            returnButton.setOnAction(this::goBackMainMenu);
            codeContainer.getChildren().add(returnButton);
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(codeContainer);
            scrollPane.setFitToWidth(true);
            // Create a scene with the code container
            Scene codeScene = new Scene(scrollPane, 400, 600);
            codeStage.setScene(codeScene);
            codeStage.show();

            // Hide the current window
//    viewPolicyHolderClaimsButton.getScene().getWindow().hide();
        } else {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist");
            return false;
        }
        return false;
    }

    @Override
    public boolean updatePolicyHolderClaim() {
        try {
            String policyHolderId = textFieldPolicyHolderID.getText();
            String claimID = textFieldClaimID.getText();
            double claimAmount = Double.parseDouble(textFieldClaimAmount.getText());
            String bankName = textFieldBankName.getText();
            String ownerName = textFieldOwnerName.getText();
            String accountNumber = textFieldAccountNumber.getText();

            if (policyHolderId.isEmpty() || claimID.isEmpty() || bankName.isEmpty() || ownerName.isEmpty() || accountNumber.isEmpty()) {
                // If any required field is empty, show an alert message
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return false; // Abort the update operation
            }

            SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
            Session session = sessionFactory.openSession();
            try {
                // Start a transaction
                session.beginTransaction();

                // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
                String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId AND h.id = :policyHolderID";
                List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                        .setParameter("policyOwnerId", policyOwner.getId())
                        .setParameter("policyHolderID", policyHolderId)
                        .getResultList();

                if (policyHolderList.isEmpty()) {
                    ShowAlert showAlert = new ShowAlert();
                    showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist.");
                    return false;
                }

                String desiredClaim = "SELECT c FROM Claim c JOIN c.policyHolder h WHERE h.id = :policyHolderId AND c.dependent IS NULL AND c.id = :claimId";
                List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                        .setParameter("policyHolderId", policyHolderId)
                        .setParameter("claimId", claimID)
                        .getResultList();

                if (claimList.isEmpty()) {
                    ShowAlert showAlert = new ShowAlert();
                    showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Claim does not exist.");
                    return false;
                }

                Claim claim = claimList.get(0);
                if (!claimList.isEmpty()){
                    claim.setClaimAmount(claimAmount);
                }
                BankInfo bankInfo = claim.getBankInfo();
                if (!bankName.isEmpty()){
                    bankInfo.setBankName(bankName);
                }
                if (!ownerName.isEmpty()){
                    bankInfo.setOwnerName(ownerName);
                }
                if (!accountNumber.isEmpty()){
                    bankInfo.setAccountNumber(accountNumber);
                }
                // Commit the transaction
                session.getTransaction().commit();
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Update Successfully");
                return true; // Update successful

            } catch (Exception ex) {
                // Rollback the transaction in case of an exception
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                ex.printStackTrace();
                return false;
            } finally {
                // Ensure the session is closed
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


    public boolean deleteClaimOfPolicyHolder() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            String policyHolderId = textFieldPolicyHolderID.getText();
            String claimID = textFieldClaimID.getText();

            if (policyHolderId.isEmpty() || claimID.isEmpty()) {
                // If any required field is empty, show an alert message
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return false; // Abort the update operation
            }

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId AND h.id = :policyHolderID";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .setParameter("policyHolderID", policyHolderId)
                    .getResultList();

            if (policyHolderList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist.");
                return false;
            }

            String desiredClaim = "SELECT c FROM Claim c JOIN c.policyHolder h WHERE h.id = :policyHolderId AND c.dependent IS NULL AND c.id = :claimId";
            List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                    .setParameter("policyHolderId", policyHolderId)
                    .setParameter("claimId", claimID)
                    .getResultList();

            if (claimList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Claim does not exist.");
                return false;
            }
            Claim claim = claimList.get(0);

            // Begin a transaction
            session.beginTransaction();
            session.delete(claim);
            // Commit the transaction
            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Delete Successfully");
            return true;
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();
//                    sessionFactory.close();
        }
        return false;
    }

    @Override
    public boolean fileClaimForDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            String dependentId = textFieldDependentID.getText();
            String claimID = textFieldClaimID.getText();
            double claimAmount = Double.parseDouble(textFieldClaimAmount.getText());
            String bankID = textFieldBankID.getText();
            String bankName = textFieldBankName.getText();
            String ownerName = textFieldOwnerName.getText();
            String accountNumber = textFieldAccountNumber.getText();

            if (dependentId.isEmpty() || claimID.isEmpty() || bankID.isEmpty() || bankName.isEmpty() || ownerName.isEmpty() || accountNumber.isEmpty()) {
                // If any required field is empty, show an alert message
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return false; // Abort the create operation
            }

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId AND d.id = :dependentId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .setParameter("dependentId", dependentId)
                    .getResultList();


            if (dependentList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist.");
                return false;
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
            Dependent dependent = dependentList.get(0);
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
//         List of document
            session.beginTransaction();
            session.save(bankInfo);
            session.save(claim);
            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "File Claim Successfully");
            return true;


        } catch (NumberFormatException e) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number for the claim amount.");
            return false;
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
    public boolean retrieveClaimForDependent() {
        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Dependent Claim Details");

        // Create a VBox to hold the code
        VBox codeContainerClaimDependent = new VBox();
        codeContainerClaimDependent.setPadding(new Insets(10));
        codeContainerClaimDependent.setSpacing(10);

        String dependentId = textFieldDependentID.getText();

        if (dependentId.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId AND d.id = :dependentId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .setParameter("dependentId", dependentId)
                    .getResultList();

            if (dependentList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist.");
                return false;
            }
            Dependent dependent = dependentList.get(0);
            session.beginTransaction();
            String desiredClaim = "SELECT c FROM Claim c WHERE c.dependent IS NOT NULL";
            List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                    .getResultList();
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
                                    "Bank ID: " + claim.getBankInfo().getBankID() + "\n" +
                                    "Bank Name: " + claim.getBankInfo().getBankName() + "\n" +
                                    "Owner Name: " + claim.getBankInfo().getOwnerName() + "\n" +
                                    "Bank Account Number: " + claim.getBankInfo().getAccountNumber()
                    );
                    codeContainerClaimDependent.getChildren().add(codeLabel);
                }
            }

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        Button returnButton = new Button("Return");
        returnButton.setOnAction(this::goBackMainMenu);
        codeContainerClaimDependent.getChildren().add(returnButton);

        // Create a ScrollPane and set the VBox as its content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(codeContainerClaimDependent);
        scrollPane.setFitToWidth(true);

        // Create a scene with the scroll pane
        Scene codeScene = new Scene(scrollPane, 400, 600); // Adjust height as needed
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
        // viewPolicyHolderClaimsButton.getScene().getWindow().hide();
        return false;
    }


    @Override
    public boolean updateClaimForDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            String dependentId = textFieldDependentID.getText();
            String claimID = textFieldClaimID.getText();
            double claimAmount = Double.parseDouble(textFieldClaimAmount.getText());
            String bankName = textFieldBankName.getText();
            String ownerName = textFieldOwnerName.getText();
            String accountNumber = textFieldAccountNumber.getText();

            if (dependentId.isEmpty() || claimID.isEmpty() || bankName.isEmpty() || ownerName.isEmpty() || accountNumber.isEmpty()) {
                // If any required field is empty, show an alert message
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return false; // Abort the create operation
            }

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId AND d.id = :dependentId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .setParameter("dependentId", dependentId)
                    .getResultList();

            if (dependentList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist.");
                return false;
            }

            session.beginTransaction();
            List<Claim> claimList = session.createQuery("FROM Claim WHERE claimId = :claimID", Claim.class)
                    .setParameter("claimID", claimID)
                    .getResultList();

            if (claimList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Claim does not exist.");
                return false;
            }

            Claim claim = claimList.get(0);

                claim.setClaimAmount(claimAmount);

            BankInfo bankInfo = claim.getBankInfo();
            if (!bankName.isEmpty()){
                bankInfo.setBankName(bankName);
            }
            if (!ownerName.isEmpty()){
                bankInfo.setOwnerName(ownerName);
            }
            if (!accountNumber.isEmpty()){
                bankInfo.setAccountNumber(accountNumber);
            }

            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Update Successfully");
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

    public boolean deleteClaimOfDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            String dependentId = textFieldDependentID.getText();
            String claimID = textFieldClaimID.getText();

            if (dependentId.isEmpty() || claimID.isEmpty()) {
                // If any required field is empty, show an alert message
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return false; // Abort the create operation
            }

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId AND d.id = :dependentId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .setParameter("dependentId", dependentId)
                    .getResultList();

            if (dependentList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist.");
                return false;
            }

            List<Claim> claimList = session.createQuery("FROM Claim WHERE claimId = :claimID", Claim.class)
                    .setParameter("claimID", claimID)
                    .getResultList();

            if (claimList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Claim does not exist.");
                return false;
            }

            Claim claim = claimList.get(0);
            // Begin a transaction
            session.beginTransaction();
            session.delete(claim);
            // Commit the transaction
            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Delete Successfully");
            return true; // Update successful


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

    public boolean createPolicyHolder() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        String policyHolderId = textFieldPolicyHolderID.getText();
        String password = textFieldPassword.getText();
        String fullName = textFieldFullName.getText();
        String cardNum = textFieldCardNumber.getText();
        String InputExpirationDate = textFieldExpirationDate.getText();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ExpirationDate = null;
        try {
            // Parse the user input into a LocalDate object using the defined format
            ExpirationDate = LocalDate.parse(InputExpirationDate, dateFormatter);
        } catch (Exception e) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Invalid date format. Please enter date in YYYY-MM-DD format.");
        }

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId AND h.id = :policyHolderID";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .setParameter("policyHolderID", policyHolderId)
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
        insuranceCard.setExpirationDate(ExpirationDate);
        insuranceCard.setCardHolder(policyHolderId);
        insuranceCard.setPolicyOwner(policyOwner);
        PolicyHolder policyHolder = new PolicyHolder();
        policyHolder.setCustomerId(policyHolderId);
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
            session.save(policyHolder);// or session.persist(policyHolder)

            // Commit the transaction
            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Create Policy Holder Successfully");
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
    public boolean showPolicyHolderInfo() {
        return false;
    }

    @Override
    public boolean updatePolicyHolderInfo() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            String policyHolderId = textFieldPolicyHolderID.getText();
            String newPolicyHolderName = textFieldFullName.getText();
            String newPassword = textFieldPassword.getText();

            if (policyHolderId.isEmpty() || newPolicyHolderName.isEmpty() || newPassword.isEmpty()) {
                // If any required field is empty, show an alert message
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return false; // Abort the create operation
            }

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId AND h.id = :policyHolderID";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .setParameter("policyHolderID", policyHolderId)
                    .getResultList();

            if (policyHolderList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist");
                return false;
            }
            PolicyHolder policyHolder = policyHolderList.get(0);

            session = sessionFactory.openSession();
            session.beginTransaction();

            policyHolder = session.get(PolicyHolder.class, policyHolderId);
            if (!newPolicyHolderName.isEmpty()){
                policyHolder.setFullName(newPolicyHolderName);
            }
            if (!newPassword.isEmpty()){
                policyHolder.setPassword(newPassword);
            }

            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Update Policy Holder Successfully");
            return true; // Update successful

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            if (session != null) {
                session.close();
            }
        }
        return false;
    }

    public boolean deletePolicyHolder() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            String policyHolderId = textFieldPolicyHolderID.getText();

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId AND h.id = :policyHolderID";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .setParameter("policyHolderID", policyHolderId)
                    .getResultList();

            if (policyHolderList.isEmpty()) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist");
                return false;
            }

            PolicyHolder policyHolder = policyHolderList.get(0);
            // Begin a transaction
            session.beginTransaction();

            session.delete(policyHolder);

            // Commit the transaction
            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Delete Policy Holder Successfully");
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

    public boolean createDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        String policyHolderId = textFieldPolicyHolderID.getText();
        String dependentId = textFieldDependentID.getText();
        String password = textFieldPassword.getText();
        String fullName = textFieldFullName.getText();
        String InputExpirationDate = textFieldExpirationDate.getText();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ExpirationDate = null;
        try {
            // Parse the user input into a LocalDate object using the defined format
            ExpirationDate = LocalDate.parse(InputExpirationDate, dateFormatter);
//            // Print the parsed LocalDate object
//            System.out.println("Parsed date: " + ExpirationDate);
        } catch (Exception e) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Invalid date format. Please enter date in YYYY-MM-DD format.");
        }

        try {
            // Parse the user input into a LocalDate object using the defined format
            ExpirationDate = LocalDate.parse(InputExpirationDate, dateFormatter);

            // Print the parsed LocalDate object
            System.out.println("Parsed date: " + ExpirationDate);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter date in YYYY-MM-DD format.");
        }

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId AND h.id = :policyHolderID";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .setParameter("policyHolderID", policyHolderId)
                .getResultList();

        if (policyHolderList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist");
            return false;
        }
        PolicyHolder policyHolder = policyHolderList.get(0);
        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId AND d.id = :dependentID";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .setParameter("dependentID", dependentId)
                .getResultList();

        if (!dependentList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent has been created");
            return false;
        }
        Dependent dependent = new Dependent();
        dependent.setCustomerId(dependentId);
        dependent.setPassword(password);
        dependent.setFullName(fullName);
        dependent.setPolicyOwner(policyOwner);
        dependent.setPolicyHolder(policyHolder);

        try {
            // Begin a transaction
            session.beginTransaction();
            session.save(dependent);//
            // Commit the transaction
            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Create Dependent Successfully");
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
    public boolean getAllDependent() {

        return false;
    }

    @Override
    public boolean updateInfoForDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        String dependentId = textFieldDependentID.getText();
        String newDependentName = textFieldFullName.getText();
        String newPassword = textFieldPassword.getText();

        if (dependentId.isEmpty() || newDependentName.isEmpty() || newPassword.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId AND d.id = :dependentID";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .setParameter("dependentID", dependentId)
                .getResultList();

        if (dependentList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist");
            return false;
        }
        Dependent dependent = dependentList.get(0);

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            dependent = session.get(Dependent.class, dependentId);
            if (!newDependentName.isEmpty()){
                dependent.setFullName(newDependentName);
            }
            if (!newPassword.isEmpty()){
                dependent.setPassword(newPassword);
            }

            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Create Dependent Successfully");
            return true; // Update successful
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            if (session != null) {
                session.close();
            }
        }


        return false;
    }

    public boolean deleteDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        String dependentId = textFieldDependentID.getText();

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId AND d.id = :dependentID";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .setParameter("dependentID", dependentId)
                .getResultList();

        if (dependentList.isEmpty()) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist");
            return false;
        }
        Dependent dependent = dependentList.get(0);
        try {
            // Begin a transaction
            session.beginTransaction();

            session.delete(dependent);

            // Commit the transaction
            session.getTransaction().commit();
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Delete Dependent Successfully");
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

    public double calcInsuranceFee() {
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
