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
@DiscriminatorValue("PolicyHolder") // Discriminator value for PolicyHolder
public class PolicyHolder extends Customer  implements CRUDoperation {
    @Id
    @Column(name = "PolicyHolderId")
    private String policyHolderId;

    @Column(name = "insuranceCardNumber")
    private String insuranceCardNumber;

    @Column(name = "PolicyOwnerId")
    private String policyOwnerId;

    public PolicyHolder(String policyHolderId, String insuranceCardNumber, String policyOwnerId) {
        this.policyHolderId = policyHolderId;
        this.insuranceCardNumber = insuranceCardNumber;
        this.policyOwnerId = policyOwnerId;
    }

    public PolicyHolder() {}

    public String getPolicyHolderId() {
        return policyHolderId;
    }

    public void setPolicyHolderId(String policyHolderId) {
        this.policyHolderId = policyHolderId;
    }

    public String getInsuranceCardNumber() {
        return insuranceCardNumber;
    }

    public void setInsuranceCardNumber(String insuranceCardNumber) {
        this.insuranceCardNumber = insuranceCardNumber;
    }

    public String getPolicyOwnerId() {
        return policyOwnerId;
    }

    public void setPolicyOwnerId(String policyOwnerId) {
        this.policyOwnerId = policyOwnerId;
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
        Customer customer = new PolicyHolder(userName, password, fullName);
        PolicyHolder policyHolder = new PolicyHolder();
        InsuranceCard insuranceCard = new InsuranceCard(cardNum, ExpirationDate, policyOwnerId, userName);

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            session.save(customer); // or session.persist(policyHolder)
            session.save(insuranceCard);// or session.persist(insuranceCard)
            session.save(policyHolder);// or session.persist(policyHolder)

            // Commit the transaction
            session.getTransaction().commit();
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
