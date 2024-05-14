package org.example.asm2_insurance_claim_management_system.Customers;

import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Claim.Claim;
import org.example.asm2_insurance_claim_management_system.Claim.Status;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;
import org.example.asm2_insurance_claim_management_system.Interface.SuperCustomer;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
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
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();
        PolicyOwner policyOwner = new PolicyOwner();
        List<PolicyOwner> policyOwnerList = session.createQuery("FROM PolicyOwner ", PolicyOwner.class).getResultList();
        for (PolicyOwner testPolicyOwner : policyOwnerList) {
            if (testPolicyOwner.getId().equals(policyOwnerId)) {
                policyOwner = testPolicyOwner;
            } else {
                System.out.println("Policy Owner does not exist");
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


    public boolean update() {
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
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


    public boolean delete() {

//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();

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


    public boolean view() {
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
            policyHolderList = session.createQuery("FROM PolicyHolder ", PolicyHolder.class).getResultList();
            for (PolicyHolder policyHolder : policyHolderList) {
                System.out.println("PolicyHolder ID: " + policyHolder.getId());
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
//        finally {
//            // Close the session and session factory
//            session.close();
//            sessionFactory.close();
//        }
        return policyHolderList;
    }

    @Override
    public boolean fileClaim() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the claimID (6 character): ");
        String claimID = scanner.nextLine();
        System.out.println("Enter the claim amount: ");
        double claimAmount = scanner.nextDouble();


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        Claim claim = new Claim();
        claim.setClaimId(claimID);
        claim.setClaimDate(LocalDate.now());
        claim.setStatus(Status.NEW);
        claim.setInsuranceCard(this.insuranceCard);
        claim.setPolicyHolder(this);
        claim.setClaimAmount(claimAmount);
        // List of document

        try {
            session.beginTransaction();
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
    public boolean updateClaim() {

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = null;
        Scanner scanner = new Scanner(System.in);

        try {
            session = sessionFactory.openSession();

            System.out.println("Enter the Claim ID:");
            String claimID = scanner.nextLine();
            System.out.println("Enter the new Claim Amount: ");
            double claimAmount = scanner.nextDouble();
            // List of document

            session.beginTransaction();
            Claim claim = session.get(Claim.class, claimID);
            if (claimID == null) {
                System.out.println("Claim with claim ID" + claimID + "is not found");
                return false;
            }

            claim.setClaimAmount(claimAmount);

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
    public boolean retrieveClaim() {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            List<Claim> claimList = session.createQuery("FROM Claim ", Claim.class).getResultList();
            for (Claim claim : claimList) {
                if (this.getId().equals(claim.getPolicyHolder().getId())) {
                    System.out.println("Claim ID: " + claim.getClaimId());
                    System.out.println("Claim Date: " + claim.getClaimDate());
                    System.out.println("Claim Amount: " + claim.getClaimAmount());
                    System.out.println("List of Document: " + claim.getListOfDocument());
                    System.out.println("Claim Status: " + claim.getStatus());
                    System.out.println("Card Number: " + claim.getInsuranceCard().getCardNumber());
                    System.out.println("Policy Holder: " + claim.getPolicyHolder().getId());
                    System.out.println("Dependent: " + claim.getDependent());
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
    public boolean showInfo() {
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
    public boolean updateInfo() {
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


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyHolderId is the ID of the PolicyHolder you want to retrieve dependents for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder p WHERE p.id = :policyHolderId";
        List<Dependent> dependentList = session.createQuery(desiredDependent, Dependent.class)
                .setParameter("policyHolderId", this.getId())
                .getResultList();
        for (Dependent dependent : dependentList) {
            if (!dependent.getId().equals(dependentId)) {
                System.out.println("Dependent does not exist");
            } else {
                Claim claim = new Claim();
                claim.setClaimId(claimID);
                claim.setClaimDate(LocalDate.now());
                claim.setStatus(Status.NEW);
                claim.setInsuranceCard(this.insuranceCard);
                claim.setPolicyHolder(this);
                claim.setClaimAmount(claimAmount);
                claim.setDependent(dependent);
//         List of document

                try {
                    session.beginTransaction();
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


        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();

// Assuming policyHolderId is the ID of the PolicyHolder you want to retrieve dependents for
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder p WHERE p.id = :policyHolderId";
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
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder p WHERE p.id = :policyHolderId";
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
        String desiredDependent = "SELECT d FROM Dependent d JOIN d.policyHolder p WHERE p.id = :policyHolderId";
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

        List<Dependent> dependentList;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            dependentList = session.createQuery("FROM Dependent ", Dependent.class).getResultList();
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
