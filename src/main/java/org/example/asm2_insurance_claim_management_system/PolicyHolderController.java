package org.example.asm2_insurance_claim_management_system;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PolicyHolderController implements CRUDoperation {
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
    @FXML
    private Button savePolicyHolderButton;



    @FXML
    @Override
    public boolean create() {
        String userName = textFieldId.getText();
        String password = textFieldPassword.getText();
        String fullName = textFieldFullName.getText();
        String cardNum = textCardNum.getText();
        String policyHolderId = textFieldPolicyOwner.getText();
        String userExpirationDate = textFieldDate.getText();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expirationDate = null;
        try {
            // Parse the user input into a LocalDate object using the defined format
            expirationDate = LocalDate.parse(userExpirationDate, dateFormatter);

            // Print the parsed LocalDate object
            System.out.println("Parsed date: " + expirationDate);
        } catch (Exception e) {
            // If the credentials are incorrect, show an alert message
            Alert alertDateError = new Alert(Alert.AlertType.ERROR);
            alertDateError.setTitle("Date Error");
            alertDateError.setHeaderText(null);
            alertDateError.setContentText("Invalid date format. Please enter date in YYYY-MM-DD format.");
            alertDateError.showAndWait();
            return false;
        }
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        PolicyOwner policyOwner = new PolicyOwner();
        List<PolicyOwner> policyOwnerListList = session.createQuery("FROM PolicyOwner ", PolicyOwner.class).getResultList();
        for (PolicyOwner testPolicyOwner : policyOwnerListList) {
            if (testPolicyOwner.getId().equals(policyHolderId)) {
                policyOwner = testPolicyOwner;
            } 
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
            session.save(policyHolder);// or session.persist(policyHolder)

            // Commit the transaction
            session.getTransaction().commit();
            Alert alertSucess = new Alert(Alert.AlertType.INFORMATION);
            alertSucess.setTitle("Successful");
            alertSucess.setHeaderText(null);
            alertSucess.setContentText("Create Successfully");
            alertSucess.showAndWait();
            textFieldId.setText("");
            textFieldPassword.setText("");
            textFieldFullName.setText("");
            textCardNum.setText("");
            textFieldPolicyOwner.setText("");
            textFieldDate.setText("");
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

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean view() {
        return false;
    }
}
