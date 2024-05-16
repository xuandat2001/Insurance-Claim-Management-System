package org.example.asm2_insurance_claim_management_system;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Claim.Status;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.SuperCustomer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class CRUDClaim extends PolicyHolder implements SuperCustomer {

    @FXML
    private TextField textFieldClaimID;
    @FXML
    private TextField textFieldClaimAmount;
    @FXML
    private TextField textFieldDocument;
    private InsuranceCard insuranceCard;

    @FXML
    private Button saveClaimButton;

    @Override
    public boolean filePolicyHolderClaim() {
        String claimID = textFieldClaimID.getText();
        double claimAmount = Double.parseDouble(textFieldClaimAmount.getText());


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        InsuranceCard insuranceCard = new InsuranceCard();
        Claim claim = new Claim();
        claim.setClaimId(claimID);
        claim.setClaimDate(LocalDate.now());
        claim.setStatus(Status.NEW);
        claim.setInsuranceCard(this.insuranceCard);
        claim.setPolicyHolder(this);
        claim.setClaimAmount(claimAmount);
        // List of document

        try {
            session.beginTransaction();
            session.save(claim);
            session.getTransaction().commit();
            System.out.println("Create claim successfully");
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
        return false;

    }

    @Override
    public boolean updatePolicyHolderClaim() {
        return false;
    }

    @Override
    public boolean retrievePolicyHolderClaim() {
        return false;
    }

    @Override
    public boolean updatePolicyHolderInfo() {
        return false;
    }

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
}
