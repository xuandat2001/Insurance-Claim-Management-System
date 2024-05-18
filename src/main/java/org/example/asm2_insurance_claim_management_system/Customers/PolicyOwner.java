package org.example.asm2_insurance_claim_management_system.Customers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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
@Table(name = "PolicyOwner")
@PrimaryKeyJoinColumn(name = "PolicyOwnerId") // Discriminator value for PolicyOwner
public class PolicyOwner extends Customer implements UserAuthentication, SuperCustomer {


    @Column(name = "Location")
    private String location;

    @Column(name = "insuranceFee")
    private Double insuranceFee;

    private static final double DEPENDENT_FEE = 0.6;

    public PolicyOwner(String policyOwnerId, String location) {

        this.location = location;
    }

    public PolicyOwner() {
    }

    public Double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(Double insuranceFee) {
        this.insuranceFee = insuranceFee;
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

    @Override
    public boolean filePolicyHolderClaim() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Policy Holder ID: ");
        String policyHolderId = scanner.nextLine();
        System.out.println("Enter the Claim ID (6 character): ");
        String claimID = scanner.nextLine();
        System.out.println("Enter the Claim Amount: ");
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
                    String desiredClaim = "SELECT c FROM Claim c JOIN c.policyHolder h WHERE h.id = :policyHolderId AND c.dependent IS NULL";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .setParameter("policyHolderId", policyHolderId)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (!claim.getClaimId().equals(claimID)) {
                            System.out.println("Claim does not exist");
                        } else {

                            claim.setClaimAmount(claimAmount);
                            claim.getBankInfo().setBankName(bankName);
                            claim.getBankInfo().setOwnerName(ownerName);
                            claim.getBankInfo().setAccountNumber(accountNumber);

                            session.getTransaction().commit();
                            System.out.println("Update Sucessfully");
                            return true; // Update successful
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

    public boolean deletePolicyHolderClaim() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Policy Holder ID you want to delete his/her Claim: ");
        String policyHolderId = scanner.nextLine();
        System.out.println("Enter the Claim ID you want to delete: ");
        String claimID = scanner.nextLine();

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
                    String desiredClaim = "SELECT c FROM Claim c JOIN c.policyHolder h WHERE h.id = :policyHolderId AND c.dependent IS NULL";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .setParameter("policyHolderId", policyHolderId)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (!claim.getClaimId().equals(claimID)) {
                            System.out.println("Claim does not exist");
                        } else{
                    // Begin a transaction
                    session.beginTransaction();

                    session.delete(claim);

                    // Commit the transaction
                    session.getTransaction().commit();
                    System.out.println("Delete Successfully");
                    return true;}}
                } catch (Exception ex) {
                    // Rollback the transaction in case of an exception
                    session.getTransaction().rollback();
                    ex.printStackTrace();
                } finally {
                    // Close the session and session factory
                    session.close();
                    sessionFactory.close();
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
            } else {
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
        ;
// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", this.getId())
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
                claim.setInsuranceCard(dependent.getPolicyHolder().getInsuranceCard());
                claim.setPolicyHolder(dependent.getPolicyHolder());
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

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", this.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                System.out.println("Dependent does not exist");
            } else {
                try {
                    session.beginTransaction();
                    String desiredClaim = "SELECT c FROM Claim c JOIN c.dependent d WHERE d.id = :dependentId";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .setParameter("dependentId", dependentId)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (!claim.getClaimId().equals(claimID)) {
                            System.out.println("Claim does not exist");
                        } else {
                            claim.setClaimAmount(claimAmount);
                            claim.getBankInfo().setBankName(bankName);
                            claim.getBankInfo().setOwnerName(ownerName);
                            claim.getBankInfo().setAccountNumber(accountNumber);

                            session.getTransaction().commit();
                            System.out.println("Update Sucessfully");
                            return true; // Update successful
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
    public boolean retrieveClaimForDependent() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Dependent ID: ");
        String dependentId = scanner.nextLine();


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", this.getId())
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

    public boolean deleteDependentClaim() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Dependent ID you want to delete his/her Claim: ");
        String dependentId = scanner.nextLine();
        System.out.println("Enter the Claim ID you want to delete: ");
        String claimID = scanner.nextLine();

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", this.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                System.out.println("Dependent does not exist");
            } else {
                try {
                    String desiredClaim = "SELECT c FROM Claim c JOIN c.dependent d WHERE d.id = :dependentId";
                    List<Claim> claimList = session.createQuery(desiredClaim, Claim.class)
                            .setParameter("dependentId", dependentId)
                            .getResultList();
                    for (Claim claim : claimList) {
                        if (!claim.getClaimId().equals(claimID)) {
                            System.out.println("Claim does not exist");
                        } else{
                            // Begin a transaction
                            session.beginTransaction();

                            session.delete(claim);

                            // Commit the transaction
                            session.getTransaction().commit();
                            System.out.println("Delete Successfully");
                            return true;}}
                } catch (Exception ex) {
                    // Rollback the transaction in case of an exception
                    session.getTransaction().rollback();
                    ex.printStackTrace();
                } finally {
                    // Close the session and session factory
                    session.close();
                    sessionFactory.close();
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

// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", this.getId())
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

    @Override
    public boolean getAllDependent() {
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyOwnerId", this.getId())
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

    public boolean deletePolicyHolder() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Policy Holder ID you want to delete: ");
        String policyHolderId = scanner.nextLine();

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
                    // Begin a transaction
                    session.beginTransaction();

                    session.delete(policyHolder);

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
            }
        }

        return false;
    }

    public boolean deleteDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Dependent ID you want to delete: ");
        String dependentId = scanner.nextLine();

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyOwnerId", this.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                System.out.println("Dependent does not exist");
            } else {
                try {
                    // Begin a transaction
                    session.beginTransaction();

                    session.delete(dependent);

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
            }
        }
        return false;
    }

    public boolean addPolicyHolder() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Policy Holder ID: ");
        String policyHolderId = scanner.nextLine();
        System.out.println("Enter the password : ");
        String password = scanner.nextLine();
        System.out.println("Enter the full name : ");
        String fullName = scanner.nextLine();
        System.out.println("Enter the Card Number(6 digits) : ");
        String cardNum = scanner.nextLine();
        System.out.println("Enter a expirationDate (YYYY-MM-DD): ");
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
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        InsuranceCard insuranceCard = new InsuranceCard();
        insuranceCard.setCardNumber(cardNum);
        insuranceCard.setExpirationDate(ExpirationDate);
        insuranceCard.setCardHolder(policyHolderId);
        insuranceCard.setPolicyOwner(this);
        PolicyHolder policyHolder = new PolicyHolder();
        policyHolder.setCustomerId(policyHolderId);
        policyHolder.setPassword(password);
        policyHolder.setFullName(fullName);
        policyHolder.setPolicyOwner(this);

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

    public boolean addDependentForPolicyHolder() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Policy Holder ID: ");
        String policyHolderId = scanner.nextLine();
        System.out.println("Enter the Dependent ID: ");
        String dependentId = scanner.nextLine();
        System.out.println("Enter the password : ");
        String password = scanner.nextLine();
        System.out.println("Enter the full name : ");
        String fullName = scanner.nextLine();
        System.out.println("Enter a expirationDate (YYYY-MM-DD): ");
        String InputExpirationDate = scanner.nextLine();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ExpirationDate = null;

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Parse the user input into a LocalDate object using the defined format
            ExpirationDate = LocalDate.parse(InputExpirationDate, dateFormatter);

            // Print the parsed LocalDate object
            System.out.println("Parsed date: " + ExpirationDate);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter date in YYYY-MM-DD format.");
        }

        // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
        String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
        List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                .setParameter("policyOwnerId", this.getId())
                .getResultList();
        for (PolicyHolder policyHolder : policyHolderList) {
            if (!policyHolder.getId().equals(policyHolderId)) {
                System.out.println("Policy Holder does not exist");
            } else {
                Dependent dependent = new Dependent();
                dependent.setCustomerId(dependentId);
                dependent.setPassword(password);
                dependent.setFullName(fullName);
                dependent.setPolicyOwner(this);
                dependent.setPolicyHolder(policyHolder);
                try {
                    // Begin a transaction
                    session.beginTransaction();

                    session.save(dependent);// or session.persist(policyHolder)

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


            }
        }
        return false;
    }

    public int getNumberOfPolicyHolder() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve PolicyHolder for
            String desiredPolicyHolder = "SELECT h FROM PolicyHolder h JOIN h.policyOwner o WHERE o.id = :policyOwnerId";
            List<PolicyHolder> policyHolderList = session.createQuery(desiredPolicyHolder, PolicyHolder.class)
                    .setParameter("policyOwnerId", this.getId())
                    .getResultList();

            int count = policyHolderList.size();
            return count;
//            System.out.println(count);

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();
        }

        return 0;
    }

    public int getNumberOfDependent() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        try {
            // Perform a query
// Assuming policyOwnerId is the ID of the PolicyOwner you want to retrieve Dependent for
            String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyOwner o WHERE o.id = :policyOwnerId";
            List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                    .setParameter("policyOwnerId", this.getId())
                    .getResultList();

            int count = dependentList.size();
            return count;
//            System.out.println(count);

        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();
        }
        return 0;
    }

    public double calcInsuranceFee() {
        double policyHolderFee = this.getNumberOfPolicyHolder() * this.getInsuranceFee();
        double dependentFee = this.getNumberOfDependent() * this.getInsuranceFee() * DEPENDENT_FEE;
        double totalInsuranceFee = policyHolderFee + dependentFee;
        System.out.println("Total Insurance Fee have to pay yearly: " + totalInsuranceFee);
        return 0;
    }

}


