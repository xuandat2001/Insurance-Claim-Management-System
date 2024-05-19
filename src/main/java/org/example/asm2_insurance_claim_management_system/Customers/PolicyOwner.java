package org.example.asm2_insurance_claim_management_system.Customers;
/**
 * @author <Group 22>
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;
import java.util.List;

@Entity
@Table(name = "PolicyOwner")
@PrimaryKeyJoinColumn(name = "PolicyOwnerId") // Discriminator value for PolicyOwner
public class PolicyOwner extends Customer implements UserAuthentication{

    @Column(name = "Location")
    private String location;
    @Column(name = "insuranceFee")
    private Double insuranceFee;

    private static final double DEPENDENT_FEE = 0.6;

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
        finally {
            // Close the session and session factory
            session.close();
        }
        return policyOwnerList;
    }
}


