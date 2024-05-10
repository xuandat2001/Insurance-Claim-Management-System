package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Admin.Admin;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "PolicyHolder")
@PrimaryKeyJoinColumn(name = "PolicyHolderId") // Discriminator value for PolicyHolder
public class PolicyHolder extends Customer  implements CRUDoperation {

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "insuranceCardNumber") // foreign key referencing InsuranceCard's primary key
    private InsuranceCard insuranceCard;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "PolicyOwnerId") // foreign key referencing InsuranceCard's primary key
    private PolicyOwner policyOwner;

    public PolicyHolder() {
        super();
    }


    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public PolicyOwner getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(PolicyOwner policyOwner) {
        this.policyOwner = policyOwner;
    }

    @Override
    public String toString() {
        return "PolicyHolder{" +
                "insuranceCard=" + insuranceCard +
                ", policyOwner=" + policyOwner +
                ", customerId='" + customerId + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
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
        System.out.println("Set your Card Number(6 digits) : ");
        String cardNum = scanner.nextLine();
        System.out.println("Set your Policy Owner(6 digits) : ");
        String policyOwnerId = scanner.nextLine();
        System.out.println("Enter a date (YYYY-MM-DD): ");
        String InputExpirationDate = scanner.nextLine();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ExpirationDate = null;
        try {
            // Parse the user input into a LocalDate object using the defined format
            ExpirationDate = LocalDate.parse(InputExpirationDate, dateFormatter);

            // Print the parsed LocalDate object
            System.out.println("Parsed date: " + ExpirationDate);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter date in YYYY-MM-DD format.");
        }
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        PolicyOwner policyOwner = new PolicyOwner();
        List<PolicyOwner>policyOwnerListList = session.createQuery("FROM PolicyOwner ", PolicyOwner.class).getResultList();
        for (PolicyOwner testPolicyOwner: policyOwnerListList){
            if (testPolicyOwner.getCustomerId().equals(policyOwnerId)){
                policyOwner = testPolicyOwner;
            }
        }
        InsuranceCard insuranceCard = new InsuranceCard();
        insuranceCard.setCardNumber(cardNum);
        insuranceCard.setExpirationDate(ExpirationDate);
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
            PolicyHolder policyHolder = session.get(PolicyHolder.class, userName);
            if (policyHolder == null) {
                System.out.println("Customer with username " + userName + " not found.");
                return false;
            }

            // Make modifications to the entity
            policyHolder.setPassword(newPassword);
            policyHolder.setFullName(newFullName);

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
    // Create a Hibernate SessionFactory

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
            PolicyHolder policyHolder = session.get(PolicyHolder.class, userName);

            // Check if the entity exists
            if (policyHolder != null) {
                // Delete the entity
                session.delete(policyHolder);
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

        List<PolicyHolder>policyHolderList;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            policyHolderList = session.createQuery("FROM PolicyHolder ", PolicyHolder.class).getResultList();
            for (PolicyHolder policyHolder : policyHolderList ){
                System.out.println("PolicyHolder ID: " + policyHolder.getCustomerId());
                System.out.println("Full Name: " + policyHolder.getFullName());
                System.out.println("Password: " + policyHolder.getPassword());
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
