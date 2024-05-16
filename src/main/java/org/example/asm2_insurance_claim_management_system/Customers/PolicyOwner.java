package org.example.asm2_insurance_claim_management_system.Customers;

import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Claim.BankInfo;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Claim.Status;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.example.asm2_insurance_claim_management_system.Interface.SuperCustomer;
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
public class PolicyOwner extends Customer implements UserAuthentication, SuperCustomer {


    @Column(name = "Location")
    private String location;

    public PolicyOwner(String policyOwnerId, String location) {

        this.location = location;
    }

    public PolicyOwner() {
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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


    public boolean view() {

        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<PolicyOwner> policyOwnerList;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            policyOwnerList = session.createQuery("FROM PolicyOwner ", PolicyOwner.class).getResultList();
            for (PolicyOwner policyOwner : policyOwnerList) {
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
    @Override
    public boolean filePolicyHolderClaim() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the policy ID: ");
        String policyHolderId = scanner.nextLine();
        System.out.println("Enter the claimID (6 character): ");
        String claimID = scanner.nextLine();
        System.out.println("Enter the claim amount: ");
        double claimAmount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Set the Bank ID: ");
        String bankID = scanner.nextLine();
        System.out.println("Set the Bank Name: ");
        String bankName = scanner.nextLine();
        System.out.println("Set the Owner Name: ");
        String ownerName = scanner.nextLine();
        System.out.println("Set the Account Number: ");
        String accountNumber = scanner.nextLine();


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", this.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
                System.out.println("Policy Holder does not exist");
            } else {
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
//         List of document

                try {
                    session.beginTransaction();
                    session.save(bankInfo);
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

            }

        }

        return false;
    }

    @Override
    public boolean updatePolicyHolderClaim() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the policy ID: ");
        String policyHolderId = scanner.nextLine();
        System.out.println("Enter the Claim ID (6 character): ");
        String claimID = scanner.nextLine();
        System.out.println("Enter the new Claim Amount: ");
        double claimAmount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter the new Bank Name:");
        String bankName = scanner.nextLine();
        System.out.println("Enter the new Bank Owner Name:");
        String ownerName = scanner.nextLine();
        System.out.println("Enter the new Bank Account Number:");
        String accountNumber = scanner.nextLine();


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", this.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
                System.out.println("Policy Holder does not exist");
            } else {
                try {
                    session.beginTransaction();
                    Claim claim = session.get(Claim.class, claimID);
                    if (claimID == null) {
                        System.out.println("Claim with claim ID" + claimID + "is not found");
                        return false;
                    }
                    claim.setClaimAmount(claimAmount);
                    claim.getBankInfo().setBankName(bankName);
                    claim.getBankInfo().setOwnerName(ownerName);
                    claim.getBankInfo().setAccountNumber(accountNumber);

                    session.getTransaction().commit();
                    System.out.println("Update Sucessfully");
                    return true; // Update successful

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

        return false;
    }

    @Override
    public boolean retrievePolicyHolderClaim() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Policy Holder ID: ");
        String policyHolderId = scanner.nextLine();


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", this.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
                System.out.println("Policy Holder does not exist");
            } else {
                try {
                    session.beginTransaction();
                    String desiredClaim = "SELECT c FROM Claim c WHERE c.policyHolder IS NOT NULL AND c.dependent IS NULL";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (policyHolder.getId().equals(claim.getPolicyHolder().getId())) {
                            System.out.println("Claim ID: " + claim.getClaimId());
                            System.out.println("Claim Date: " + claim.getClaimDate());
                            System.out.println("Claim Amount: " + claim.getClaimAmount());
                            System.out.println("List of Document: " + claim.getListOfDocument());
                            System.out.println("Claim Status: " + claim.getStatus());
                            System.out.println("Card Number: " + claim.getInsuranceCard().getCardNumber());
                            System.out.println("Policy Holder: " + claim.getPolicyHolder().getId());
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
//            session.close();
//            sessionFactory.close();
                    if (session != null) {
                        session.close();
                    }
                }

            }

        }
        return false;
    }

    @Override
    public boolean updatePolicyHolderInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Policy Holder ID: ");
        String policyHolderId = scanner.nextLine();
        System.out.println("Enter the full name: ");
        String newDependentName = scanner.nextLine();
        System.out.println("Enter the password: ");
        String newPassword = scanner.nextLine();


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", this.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
                System.out.println("Policy Holder does not exist");
            } else  {
                try {
                    session = sessionFactory.openSession();
                    session.beginTransaction();

                    policyHolder = session.get(PolicyHolder.class, policyHolderId);
                    policyHolder.setFullName(newDependentName);
                    policyHolder.setPassword(newPassword);

                    session.getTransaction().commit();
                    System.out.println("Update Sucessfully");
                    return true; // Update successful
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

        return false;
    }

    @Override
    public boolean showPolicyHolderInfo() {
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();


        try {
            // Begin a transaction
            session.beginTransaction();

            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyOwnerId", this.getId())
                    .getResultList();
            for (PolicyHolder policyHolder : policyHolderList) {
                System.out.println("Policy Holder ID: " + policyHolder.getId());
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

    @Override
    public boolean getAllDependent() {
        return false;
    }
}
