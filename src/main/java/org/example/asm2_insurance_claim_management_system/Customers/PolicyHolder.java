package org.example.asm2_insurance_claim_management_system.Customers;

import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Claim.BankInfo;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Claim.Status;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.SuperCustomer;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "PolicyHolder")
@PrimaryKeyJoinColumn(name = "PolicyHolderId") // Discriminator value for PolicyHolder
public class PolicyHolder extends Customer implements SuperCustomer, UserAuthentication {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "insuranceCardNumber") // foreign key referencing InsuranceCard's primary key
    private InsuranceCard insuranceCard;

    @ManyToOne
    @JoinColumn(name = "PolicyOwnerId")
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

    public List<PolicyHolder> listOfPolicyHolder() {
        // Create a Hibernate SessionFactory
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<PolicyHolder> policyHolderList = null;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            policyHolderList = session.createQuery("FROM PolicyHolder", PolicyHolder.class).getResultList();

            // Commit the transaction
            session.getTransaction().commit();
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        }
        finally {
            // Close the session and session factory
            session.close();

        }
        return policyHolderList;
    }

    @Override
    public boolean filePolicyHolderClaim() {
        Scanner scanner = new Scanner(System.in);
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

        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankID(bankID);
        bankInfo.setBankName(bankName);
        bankInfo.setOwnerName(ownerName);
        bankInfo.setAccountNumber(accountNumber);
        Claim claim = new Claim();
        claim.setClaimId(claimID);
        claim.setClaimDate(LocalDate.now());
        claim.setStatus(Status.NEW);
        claim.setInsuranceCard(this.insuranceCard);
        claim.setPolicyHolder(this);
        claim.setClaimAmount(claimAmount);
        claim.setBankInfo(bankInfo);
        // List of document


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
        return false;

    }

    @Override
    public boolean updatePolicyHolderClaim() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;
        Scanner scanner = new Scanner(System.in);

        try {
            session = sessionFactory.openSession();

            System.out.println("Enter the Claim ID:");
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
            // List of document

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
    public boolean retrievePolicyHolderClaim() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            String desiredClaim = "SELECT c FROM Claim c WHERE c.dependent IS NULL";
            List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                    .getResultList();
            for (Claim claim : claimList) {
                if (this.getId().equals(claim.getPolicyHolder().getId())) {
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
            session.close();
            sessionFactory.close();
        }
        return false;
    }


    @Override
    public boolean showPolicyHolderInfo() {
        // Create a Hibernate SessionFactory
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<PolicyHolder> policyHolderList;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query

            System.out.println("PolicyHolder ID: " + this.getId());
            System.out.println("Full Name: " + this.getFullName());
            System.out.println("Password: " + this.getPassword());
            System.out.println("Policy Owner: " + this.getPolicyOwner().getId());
            System.out.println("Insurance Card: " + this.getInsuranceCard().getCardNumber());


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
    public boolean updatePolicyHolderInfo() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;
        Scanner scanner = new Scanner(System.in);

        try {
            session = sessionFactory.openSession();

            session.beginTransaction();

            System.out.println("Enter the full name: ");
            String newPolicyHolderName = scanner.nextLine();
            System.out.println("Enter the password: ");
            String newPassword = scanner.nextLine();

            PolicyHolder policyHolder = session.get(PolicyHolder.class, this.getId());
            policyHolder.setFullName(newPolicyHolderName);
            policyHolder.setPassword(newPassword);

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
    public boolean fileClaimForDependent() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the dependent ID: ");
        String dependentId = scanner.nextLine();
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

// Assuming policyHolderId is the ID of the PolicyHolder you want to retrieve dependents for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyHolderId", this.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                System.out.println("Dependent does not exist");
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
                claim.setInsuranceCard(this.insuranceCard);
                claim.setPolicyHolder(this);
                claim.setClaimAmount(claimAmount);
                claim.setDependent(dependent);
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
    public boolean updateClaimForDependent() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Dependent ID: ");
        String dependentId = scanner.nextLine();
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

// Assuming policyHolderId is the ID of the PolicyHolder you want to retrieve dependents for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyHolderId", this.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                System.out.println("Dependent does not exist");
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
    public boolean retrieveClaimForDependent() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Dependent ID: ");
        String dependentId = scanner.nextLine();


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyHolderId is the ID of the PolicyHolder you want to retrieve dependents for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyHolderId", this.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                System.out.println("Dependent does not exist");
            } else {
                try {
                    session.beginTransaction();
                    String desiredClaim = "SELECT c FROM Claim c WHERE c.dependent IS NOT NULL";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (dependent.getId().equals(claim.getDependent().getId())) {
                            System.out.println("Claim ID: " + claim.getClaimId());
                            System.out.println("Claim Date: " + claim.getClaimDate());
                            System.out.println("Claim Amount: " + claim.getClaimAmount());
                            System.out.println("List of Document: " + claim.getListOfDocument());
                            System.out.println("Claim Status: " + claim.getStatus());
                            System.out.println("Card Number: " + claim.getInsuranceCard().getCardNumber());
                            System.out.println("Policy Holder: " + claim.getPolicyHolder().getId());
                            System.out.println("Dependent: " + claim.getDependent());
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
    public boolean updateInfoForDependent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Dependent ID: ");
        String dependentId = scanner.nextLine();
        System.out.println("Enter the full name: ");
        String newDependentName = scanner.nextLine();
        System.out.println("Enter the password: ");
        String newPassword = scanner.nextLine();


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyHolderId is the ID of the PolicyHolder you want to retrieve dependents for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyHolderId", this.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                System.out.println("Dependent does not exist");
            } else {
                try {
                    session = sessionFactory.openSession();
                    session.beginTransaction();

                    dependent = session.get(Dependent.class, dependentId);
                    dependent.setFullName(newDependentName);
                    dependent.setPassword(newPassword);

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

    public boolean getAllDependent() {
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            // Assuming policyHolderId is the ID of the PolicyHolder you want to retrieve dependents for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder h WHERE h.id = :policyHolderId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyHolderId", this.getId())
                    .getResultList();
            for (Dependent dependent : dependentList) {
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

}
