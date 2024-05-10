package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "Dependent")
@PrimaryKeyJoinColumn(name = "DependentId")// Discriminator value for Dependent
public class Dependent extends Customer implements CRUDoperation, UserAuthentication {


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PolicyHolderId") // foreign key referencing InsuranceCard's primary key
    private PolicyHolder policyHolder;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
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

    @Override
    public boolean create() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Set your ID(username): ");
        String userName = scanner.nextLine();
        System.out.println("Set your password : ");
        String password = scanner.nextLine();
        System.out.println("Set your full name : ");
        String fullName = scanner.nextLine();
        System.out.println("Enter the PolicyOwner Id: ");
        String policyOwnerId = scanner.nextLine();
        System.out.println("Enter the PolicyHoler Id: ");
        String policyHolderId = scanner.nextLine();
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Dependent dependent = new Dependent();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        PolicyOwner policyOwner = new PolicyOwner();
        List<PolicyOwner> policyOwnerList = session.createQuery("FROM PolicyOwner ", PolicyOwner.class).getResultList();
        for (PolicyOwner testPolicyOwner: policyOwnerList){
            if (testPolicyOwner.getId().equals(policyOwnerId)){
                policyOwner = testPolicyOwner;
            }
            else {
                System.out.println("This PolicyOwner does not exist");
            }
        }
        PolicyHolder policyHolder = new PolicyHolder();
        List<PolicyHolder>policyHolderList = session.createQuery("FROM PolicyHolder ", PolicyHolder.class).getResultList();

        for (PolicyHolder testPolicyHolder: policyHolderList){
            if (testPolicyHolder.getId().equals(policyHolderId)){
                policyHolder = testPolicyHolder;
            }
            else {
                System.out.println("This PolicyHolder does not exist");
            }
        }
        dependent.setCustomerId(userName);
        dependent.setPassword(password);
        dependent.setFullName(fullName);
        dependent.setPolicyHolder(policyHolder);
        dependent.setPolicyOwner(policyOwner);
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            session.save(dependent);


            // Commit the transaction
            session.getTransaction().commit();
            System.out.println("Create Successfully");
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
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Session session = null;
        Scanner scanner = new Scanner(System.in);

        try {
            session = sessionFactory.openSession();
            System.out.println("Set your ID(username): ");
            String userName = scanner.nextLine();
            System.out.println("Enter your new password: ");
            String newPassword = scanner.nextLine();
            System.out.println("Enter your new FullName: ");
            String newFullName = scanner.nextLine();


            session.beginTransaction();

            // Retrieve the entity to update
            Dependent dependent = session.get(Dependent.class, userName);
            if (dependent == null) {
                System.out.println("Dependent with username " + userName + " not found.");
                return false;
            }

            // Make modifications to the entity
            dependent.setPassword(newPassword);
            dependent.setFullName(newFullName);


            // Commit the transaction
            session.getTransaction().commit();
            System.out.println("Update Sucessfully");
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
            sessionFactory.close();
        }
    }

    @Override
    public boolean delete() {
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID(username) of the record you want to delete: ");
        String userName = scanner.nextLine();
        try {
            // Begin a transaction
            session.beginTransaction();

            // Load the entity you want to delete
            Dependent dependent = session.get(Dependent.class, userName);

            // Check if the entity exists
            if (dependent != null) {
                // Delete the entity
                session.delete(dependent);
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("Record with ID " + userName + " not found.");
            }

            // Commit the transaction
            session.getTransaction().commit();
            System.out.println("Delete Successfully");
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
    public boolean view() {
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<Dependent>dependentList;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            dependentList = session.createQuery("FROM Dependent ", Dependent.class).getResultList();
            for (Dependent dependent : dependentList ){
                System.out.println("Dependent ID: " + dependent.getId());
                System.out.println("Full Name: " + dependent.getFullName());
                System.out.println("Password: " + dependent.getPassword());
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


