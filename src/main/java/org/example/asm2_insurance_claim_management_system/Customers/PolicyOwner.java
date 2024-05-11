package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;
import org.example.asm2_insurance_claim_management_system.Login.Authentication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "PolicyOwner")
@PrimaryKeyJoinColumn(name = "PolicyOwnerId") // Discriminator value for PolicyOwner
public class PolicyOwner extends Customer implements CRUDoperation, UserAuthentication {


    @Column(name = "Location")
    private String location;

    public PolicyOwner(String policyOwnerId, String location) {

        this.location = location;
    }

    public PolicyOwner() {}



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        System.out.println("Set your Location : ");
        String location = scanner.nextLine();
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        PolicyOwner policyOwner = new PolicyOwner();
        policyOwner.setCustomerId(userName);
        policyOwner.setPassword(password);
        policyOwner.setFullName(fullName);
        policyOwner.setLocation(location);

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            session.save(policyOwner);


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
            System.out.println("Enter your new Location: ");
            String newLocation = scanner.nextLine();

            session.beginTransaction();

            // Retrieve the entity to update
            PolicyOwner policyOwner = session.get(PolicyOwner.class, userName);
            if (policyOwner == null) {
                System.out.println("Customer with username " + userName + " not found.");
                return false;
            }

            // Make modifications to the entity
            policyOwner.setPassword(newPassword);
            policyOwner.setFullName(newFullName);
            policyOwner.setLocation(newLocation);

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
            PolicyOwner policyOwner = session.get(PolicyOwner.class, userName);

            // Check if the entity exists
            if (policyOwner != null) {
                // Delete the entity
                session.delete(policyOwner);
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

        List<PolicyOwner>policyOwnerList;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            policyOwnerList = session.createQuery("FROM PolicyOwner ", PolicyOwner.class).getResultList();
            for (PolicyOwner policyOwner : policyOwnerList ){
                System.out.println("PolicyOwner ID: " + policyOwner.getId());
                System.out.println("Full Name: " + policyOwner.getFullName());
                System.out.println("Password: " + policyOwner.getPassword());
                System.out.println("Location: " + policyOwner.getLocation());
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

    public List<PolicyOwner> listOfPolicyOwner() {
        // Create a Hibernate SessionFactory
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<PolicyOwner> policyOwnerList = null;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            policyOwnerList = session.createQuery("FROM PolicyOwner", PolicyOwner.class).getResultList();

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
        return policyOwnerList;
    }

}
