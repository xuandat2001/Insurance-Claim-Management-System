package org.example.asm2_insurance_claim_management_system.Providers;

import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Claim.Status;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.HibernateSingleton;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;


@Entity
@Table(name = "Manager")
@PrimaryKeyJoinColumn(name = "ManagerID")
public class Manager extends Providers {

    public Manager() {

    }


    public List<Manager> ListOfManager(){
        // Create a Hibernate SessionFactory
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<Manager> ListOfManager = null;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            ListOfManager = session.createQuery("FROM Manager", Manager.class).getResultList();

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
        return ListOfManager;
    }

    public boolean approveClaim() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;
        Scanner scanner = new Scanner(System.in);

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // List of documents
            String desiredClaimList = "SELECT c FROM Claim c WHERE c.status = :status";
            List<Claim> claimList = session.createQuery(desiredClaimList, Claim.class)
                    .setParameter("status", Status.PROCESSING)
                    .getResultList();

            for (Claim claim : claimList) {
                claim.showInfo();
            }

            System.out.println("Enter a claimID you want to approve: ");
            String claimId = scanner.nextLine();

            Claim claim = session.get(Claim.class, claimId);
            if (claim != null) {
                System.out.println(claim.getClaimId() + " is successfully approved");
                claim.setStatus(Status.DONE);
                claim.setApproval("YES");
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
            // Close the scanner
            scanner.close();
        }
    }

    public boolean rejectClaim() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;
        Scanner scanner = new Scanner(System.in);

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // List of documents
            String desiredClaimList = "SELECT c FROM Claim c WHERE c.status = :status";
            List<Claim> claimList = session.createQuery(desiredClaimList, Claim.class)
                    .setParameter("status", Status.PROCESSING)
                    .getResultList();

            for (Claim claim : claimList) {
                claim.showInfo();
            }

            System.out.println("Enter a claimID you want to reject: ");
            String claimId = scanner.nextLine();
            System.out.println("Reason for the final decision: ");
            String comment = scanner.nextLine();

            Claim claim = session.get(Claim.class, claimId);
            if (claim != null) {
                System.out.println(claim.getClaimId() + " is successfully rejected");
                claim.setStatus(Status.DONE);
                claim.setApproval("NO");
                claim.setRequiredinfo("The manager has rejected your claim, reason being:" + comment);
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
            // Close the scanner
            scanner.close();
        }
    }

    public boolean create() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Set your ID(username): ");
        String userName = scanner.nextLine();
        System.out.println("Set your password : ");
        String password = scanner.nextLine();
        System.out.println("Set your full name : ");
        String fullName = scanner.nextLine();
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Manager manager = new Manager();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        manager.setProviderId(userName);
        manager.setPassword(password);
        manager.setProviderName(fullName);

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            session.save(manager);


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
            System.out.println("Enter your new Name: ");
            String newName = scanner.nextLine();


            session.beginTransaction();

            // Retrieve the entity to update
            Manager manager = session.get(Manager.class, userName);
            if (manager == null) {
                System.out.println("Customer with username " + userName + " not found.");
                return false;
            }

            // Make modifications to the entity
            manager.setPassword(newPassword);
            manager.setProviderName(newName);


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
            Manager manager = session.get(Manager.class, userName);

            // Check if the entity exists
            if (manager != null) {
                // Delete the entity
                session.delete(manager);
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


    public boolean view() {
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<Manager>managerList;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            managerList = session.createQuery("FROM Manager ", Manager.class).getResultList();
            for (Manager manager : managerList ){
                System.out.println("Manager ID: " + manager.getProviderId());
                System.out.println("Full Name: " + manager.getProviderName());
                System.out.println("Password: " + manager.getPassword());
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
}
