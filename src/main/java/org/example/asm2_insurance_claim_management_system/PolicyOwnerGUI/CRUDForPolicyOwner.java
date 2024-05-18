package org.example.asm2_insurance_claim_management_system.PolicyOwnerGUI;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CRUDForPolicyOwner extends PolicyOwner implements SuperCustomer {

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
                String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
                List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                        .setParameter("policyOwnerId", policyOwner.getId())
                        .getResultList();

                for (PolicyHolder policyHolder : policyHolderList) {
                    if (policyHolder.getId().equals(policyHolderId)) {
                        List<Claim> claimList = session.createQuery("FROM Claim WHERE claimId = :claimID", Claim.class)
                                .setParameter("claimID", claimID)
                                .getResultList();

                        if (claimList.isEmpty()) {
                            BankInfo bankInfo = new BankInfo();
                            bankInfo.setBankID(bankID);
                            bankInfo.setBankName(bankName);
                            bankInfo.setOwnerName(ownerName);
                            bankInfo.setAccountNumber(accountNumber);

                            Claim claim = new Claim();
                            claim.setClaimId(claimID);
                            claim.setClaimDate(LocalDate.now());
                            claim.setStatus(Status.NEW);
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
                        } else {
                            ShowAlert showAlert = new ShowAlert();
                            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Claim with this ID already exists.");
                            return false;
                        }
                    }
                }
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist.");
                return false;

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

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (policyHolder.getId().equals(policyHolderId)) {
                try {
                    session.beginTransaction();
                    String desiredClaim = "SELECT c FROM Claim c WHERE c.policyHolder IS NOT NULL AND c.dependent IS NULL";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .getResultList();
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
                // Create a scene with the code container
                Scene codeScene = new Scene(codeContainer, 400, 1000);
                codeStage.setScene(codeScene);
                codeStage.show();

                // Hide the current window
//    viewPolicyHolderClaimsButton.getScene().getWindow().hide();

            }
        }
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder does not exist");
                break;
            }
        }
        return false;
    }

    @Override
    public boolean updatePolicyHolderClaim() {
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
            return false; // Abort the create operation
        }

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (policyHolder.getId().equals(policyHolderId)) {
                try {
                    String desiredClaim = "SELECT c FROM Claim c JOIN c.policyHolder h WHERE h.id = :policyHolderId AND c.dependent IS NULL";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .setParameter("policyHolderId", policyHolderId)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (claim.getClaimId().equals(claimID)) {
                            {
                                session.beginTransaction();
                                claim.setClaimAmount(claimAmount);
                                claim.getBankInfo().setBankName(bankName);
                                claim.getBankInfo().setOwnerName(ownerName);
                                claim.getBankInfo().setAccountNumber(accountNumber);

                                session.getTransaction().commit();
                                ShowAlert showAlert = new ShowAlert();
                                showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Update Successfully");
                                return true; // Update successful
                            }
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

            }

        }
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder or Claim does not exist");
                break;
            }
        }

        return false;
    }

    public boolean deleteClaimOfPolicyHolder() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        String policyHolderId = textFieldPolicyHolderID.getText();
        String claimID = textFieldClaimID.getText();

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (policyHolder.getId().equals(policyHolderId)) {
                try {
                    String desiredClaim = "SELECT c FROM Claim c JOIN c.policyHolder h WHERE h.id = :policyHolderId AND c.dependent IS NULL";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .setParameter("policyHolderId", policyHolderId)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (claim.getClaimId().equals(claimID)) {
                            // Begin a transaction
                            session.beginTransaction();

                            session.delete(claim);

                            // Commit the transaction
                            session.getTransaction().commit();
                            ShowAlert showAlert = new ShowAlert();
                            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Delete Successfully");
                            return true;
                        }
                    }
                } catch (Exception ex) {
                    // Rollback the transaction in case of an exception
                    session.getTransaction().rollback();
                    ex.printStackTrace();
                } finally {
                    // Close the session and session factory
                    session.close();
//                    sessionFactory.close();
                }
            }
        }
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Policy Holder or Claim does not exist");
                break;
            }
        }
        return false;
    }


    @Override
    public boolean fileClaimForDependent() {
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

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        ;
// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (dependent.getId().equals(dependentId)) {
                BankInfo bankInfo = new BankInfo();
                bankInfo.setBankID(bankID);
                bankInfo.setBankName(bankName);
                bankInfo.setOwnerName(ownerName);
                bankInfo.setAccountNumber(accountNumber);
                Claim claim = new Claim();
                claim.setClaimId(claimID);
                claim.setClaimDate(LocalDate.now());
                claim.setStatus(Status.NEW);
                claim.setInsuranceCard(dependent.getPolicyHolder().getInsuranceCard());
                claim.setPolicyHolder(dependent.getPolicyHolder());
                claim.setClaimAmount(claimAmount);
                claim.setDependent(dependent);
                claim.setBankInfo(bankInfo);
//         List of document

                try {
                    session.beginTransaction();
                    session.save(bankInfo);
                    session.save(claim);
                    session.getTransaction().commit();
                    ShowAlert showAlert = new ShowAlert();
                    showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "File Claim Successfully");
                    return true;


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

            }

        }
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent does not exist");
                break;
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
        VBox codeContainer = new VBox();
        codeContainer.setPadding(new Insets(10));
        codeContainer.setSpacing(10);

        String dependentId = textFieldDependentID.getText();

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .getResultList();
            for (Dependent dependent : dependentList) {
                if (dependent.getId().equals(dependentId)) {

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
                            codeContainer.getChildren().add(codeLabel);
                        }
                    }
                }
            }
//            ShowAlert showAlert = new ShowAlert();
//            showAlert.showAlert(Alert.AlertType.ERROR,"Error","Dependent does not exist");
//            return false;
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
        // Create a scene with the code container
        Scene codeScene = new Scene(codeContainer, 400, 1000);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
//    viewPolicyHolderClaimsButton.getScene().getWindow().hide();

        return false;
    }


    @Override
    public boolean updateClaimForDependent() {
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

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (dependent.getId().equals(dependentId)) {
                try {
                    session.beginTransaction();
                    String desiredClaim = "SELECT c FROM Claim c JOIN c.dependent d WHERE d.id = :dependentId";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .setParameter("dependentId", dependentId)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (claim.getClaimId().equals(claimID)) {
                            claim.setClaimAmount(claimAmount);
                            claim.getBankInfo().setBankName(bankName);
                            claim.getBankInfo().setOwnerName(ownerName);
                            claim.getBankInfo().setAccountNumber(accountNumber);

                            session.getTransaction().commit();
                            ShowAlert showAlert = new ShowAlert();
                            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Update Successfully");
                            return true; // Update successful
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

            }

        }
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent or Claim does not exist");
                break;
            }
        }
        return false;
    }

    public boolean deleteClaimOfDependent() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        String dependentId = textFieldDependentID.getText();
        String claimID = textFieldClaimID.getText();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (dependent.getId().equals(dependentId)) {
                try {
                    String desiredClaim = "SELECT c FROM Claim c JOIN c.dependent d WHERE d.id = :dependentId";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .setParameter("dependentId", dependentId)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (!claim.getClaimId().equals(claimID)) {
                            System.out.println("Claim does not exist");
                        } else {
                            // Begin a transaction
                            session.beginTransaction();

                            session.delete(claim);
                            // Commit the transaction
                            session.getTransaction().commit();
                            ShowAlert showAlert = new ShowAlert();
                            showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Delete Successfully");
                            return true; // Update successful
                        }
                    }
                } catch (Exception ex) {
                    // Rollback the transaction in case of an exception
                    session.getTransaction().rollback();
                    ex.printStackTrace();
                } finally {
                    // Close the session and session factory
                    session.close();
                }
            }
        }
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                ShowAlert showAlert = new ShowAlert();
                showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Dependent or Claim does not exist");
                break;
            }
        }
        return false;
    }

    public boolean createPolicyHolder(){

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
//            // Print the parsed LocalDate object
//            System.out.println("Parsed date: " + ExpirationDate);
        } catch (Exception e) {
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Invalid date format. Please enter date in YYYY-MM-DD format.");
        }
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

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
    public boolean showPolicyHolderInfo(){


        // Create a new stage (window)
        Stage codeStage = new Stage();
        codeStage.setTitle("Policy Holder Information");

        // Create a VBox to hold the code
        VBox codeContainer = new VBox();
        codeContainer.setPadding(new Insets(10));
        codeContainer.setSpacing(10);

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            // Begin a transaction
            session.beginTransaction();

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyOwnerId", policyOwner.getId())
                    .getResultList();
            for (PolicyHolder policyHolder : policyHolderList) {
                Label codeLabel = new Label(
                        "Policy Holder ID: " + policyHolder.getId() + "\n" +
                                "Full Name: " + policyHolder.getFullName() + "\n" +
                                "Password: " + policyHolder.getPassword()
                );
                codeContainer.getChildren().add(codeLabel);
            }

            // Commit the transaction
            session.getTransaction().commit();
            return true;
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
        // Create a scene with the code container
        Scene codeScene = new Scene(codeContainer, 400, 1000);
        codeStage.setScene(codeScene);
        codeStage.show();

        // Hide the current window
//    viewPolicyHolderClaimsButton.getScene().getWindow().hide();

        return false;
    }
    @Override
    public boolean updatePolicyHolderInfo() {
        String policyHolderId = textFieldPolicyHolderID.getText();
        String newDependentName = textFieldFullName.getText();
        String newPassword = textFieldPassword.getText();

        if (policyHolderId.isEmpty() || newDependentName.isEmpty() || newPassword.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
//                System.out.println("Policy Holder does not exist");
            } else {
                try {
                    session = sessionFactory.openSession();
                    session.beginTransaction();

                    policyHolder = session.get(PolicyHolder.class, policyHolderId);
                    policyHolder.setFullName(newDependentName);
                    policyHolder.setPassword(newPassword);

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

            }

        }

        return false;
    }

    public boolean deletePolicyHolder(){

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        String policyHolderId = textFieldPolicyHolderID.getText();

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
//                System.out.println("Policy Holder does not exist");
            } else {
                try {
                    // Begin a transaction
                    session.beginTransaction();

                    session.delete(policyHolder);

                    // Commit the transaction
                    session.getTransaction().commit();
                    ShowAlert showAlert = new ShowAlert();
                    showAlert.showAlert(Alert.AlertType.INFORMATION, "Successful", "Update Policy Holder Successfully");
                    return true;
                } catch (Exception ex) {
                    // Rollback the transaction in case of an exception
                    session.getTransaction().rollback();
                    ex.printStackTrace();
                } finally {
                    // Close the session and session factory
                    session.close();
                }
            }
        }

        return false;
    }

    public boolean createDependent(){

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

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Parse the user input into a LocalDate object using the defined format
            ExpirationDate = LocalDate.parse(InputExpirationDate, dateFormatter);

            // Print the parsed LocalDate object
            System.out.println("Parsed date: " + ExpirationDate);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter date in YYYY-MM-DD format.");
        }

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
//                System.out.println("Policy Holder does not exist");
            } else {
                Dependent dependent = new Dependent();
                dependent.setCustomerId(dependentId);
                dependent.setPassword(password);
                dependent.setFullName(fullName);
                dependent.setPolicyOwner(policyOwner);
                dependent.setPolicyHolder(policyHolder);
                try {
                    // Begin a transaction
                    session.beginTransaction();

                    session.save(dependent);// or session.persist(policyHolder)

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
                    sessionFactory.close();
                }


            }
        }
        return false;
    }
    @Override
    public boolean getAllDependent(){

        return false;
    }
    @Override
    public boolean updateInfoForDependent() {
        String dependentId = textFieldDependentID.getText();
        String newDependentName = textFieldFullName.getText();
        String newPassword = textFieldPassword.getText();

        if (dependentId.isEmpty() || newDependentName.isEmpty() || newPassword.isEmpty()) {
            // If any required field is empty, show an alert message
            ShowAlert showAlert = new ShowAlert();
            showAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Abort the create operation
        }
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
//                System.out.println("Dependent does not exist");
            } else {
                try {
                    session = sessionFactory.openSession();
                    session.beginTransaction();

                    dependent = session.get(Dependent.class, dependentId);
                    dependent.setFullName(newDependentName);
                    dependent.setPassword(newPassword);

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

            }

        }

        return false;
    }

    public boolean deleteDependent(){
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        String dependentId = textFieldDependentID.getText();

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", policyOwner.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
//                System.out.println("Dependent does not exist");
            } else {
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
            }
        }
        return false;
    }

    public double calcInsuranceFee(){
        return 0;
    }
}
