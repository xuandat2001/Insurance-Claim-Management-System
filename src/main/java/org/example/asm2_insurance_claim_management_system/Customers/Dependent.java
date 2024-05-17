package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "Dependent")
@PrimaryKeyJoinColumn(name = "DependentId")// Discriminator value for Dependent
public class Dependent extends Customer implements UserAuthentication {


    @OneToOne
    @JoinColumn(name = "PolicyHolderId") // foreign key referencing InsuranceCard's primary key
    private PolicyHolder policyHolder;

    @OneToOne
    @JoinColumn(name = "PolicyOwnerId") // foreign key referencing InsuranceCard's primary key
    private PolicyOwner policyOwner;
    public Dependent() {

    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    public PolicyOwner getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(PolicyOwner policyOwner) {
        this.policyOwner = policyOwner;
    }

    public List<Dependent> listOfDependent() {
        // Create a Hibernate SessionFactory
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<Dependent> dependentList = null;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            dependentList = session.createQuery("FROM Dependent", Dependent.class).getResultList();

            // Commit the transaction
            session.getTransaction().commit();
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        }
//        finally {
//            // Close the session and session factory
//            session.close();
//            sessionFactory.close();
//        }
        return dependentList;
    }

    public boolean retrieveClaim() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            String desiredClaim = "SELECT c FROM Claim c WHERE c.dependent IS NOT NULL";
            List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                    .getResultList();
            for (Claim claim : claimList) {
                if (this.getId().equals(claim.getDependent().getId())) {
                    System.out.println("Claim ID: " + claim.getClaimId());
                    System.out.println("Claim Date: " + claim.getClaimDate());
                    System.out.println("Claim Amount: " + claim.getClaimAmount());
                    System.out.println("List of Document: " + claim.getListOfDocument());
                    System.out.println("Claim Status: " + claim.getStatus());
                    System.out.println("Card Number: " + claim.getInsuranceCard().getCardNumber());
                    System.out.println("Policy Holder: " + claim.getPolicyHolder().getId());
                    System.out.println("Dependent: " + claim.getDependent().getId());
                    System.out.println("Bank ID: " + claim.getBankInfo().getBankID());
                    System.out.println("Bank Name: " + claim.getBankInfo().getBankName());
                    System.out.println("Owner Name: " + claim.getBankInfo().getOwnerName());
                    System.out.println("Bank Account Number: " + claim.getBankInfo().getAccountNumber());
                }
            }
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

    public boolean showInfo(){
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<Dependent> dependentList;
        try {
            // Begin a transaction
            session.beginTransaction();

                System.out.println("Dependent ID: " + this.getId());
                System.out.println("Full Name: " + this.getFullName());
                System.out.println("Password: " + this.getPassword());
                System.out.println("Policy Holder: " + this.getPolicyHolder().getId());
                System.out.println("Policy Owner: " + this.getPolicyOwner().getId());
                System.out.println("Insurance Card: " + this.getPolicyHolder().getInsuranceCard().getCardNumber());

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
    @Override
    public String getId() {
        return customerId;
    }
}


