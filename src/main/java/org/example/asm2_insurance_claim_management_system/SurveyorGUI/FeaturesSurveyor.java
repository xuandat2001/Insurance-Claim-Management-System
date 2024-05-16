package org.example.asm2_insurance_claim_management_system.SurveyorGUI;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Claim.Status;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Scanner;

public class FeaturesSurveyor {
    @FXML
    private TextField textClaimId;

    public boolean ProposeClaim(){
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;

        String claimId = textClaimId.getText();

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // List of documents
            String desiredClaimList = "SELECT c FROM Claim c WHERE c.status = :status";
            List<Claim> claimList = session.createQuery(desiredClaimList, Claim.class)
                    .setParameter("status", Status.NEW)
                    .getResultList();

            for (Claim claim : claimList) {
                claim.showInfo();
            }

            Claim claim = session.get(Claim.class, claimId);
            if (claim != null) {
                System.out.println(claim.getClaimId() + " will be proposed to the manager.");
                claim.setStatus(Status.PROCESSING);
                session.getTransaction().commit();
                return true; // Update successful
            } else {
                System.out.println("Claim not found.");
                session.getTransaction().rollback();
                return false; // Claim not found
            }
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            ex.printStackTrace(); // Print error details
            return false; // Update failed
        } finally {
            // Close the session
            if (session != null) {
                session.close();
            }
        }
    }
}
