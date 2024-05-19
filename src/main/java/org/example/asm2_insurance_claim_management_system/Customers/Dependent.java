package org.example.asm2_insurance_claim_management_system.Customers;
/**
 * @author <Group 22>
 */
import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;
import java.util.List;

@Entity
@Table(name = "Dependent")
@PrimaryKeyJoinColumn(name = "DependentId")// Discriminator value for Dependent
public class Dependent extends Customer implements UserAuthentication {


    @OneToOne
    @JoinColumn(name = "PolicyHolderId") // foreign key referencing InsuranceCard's primary key
    private PolicyHolder policyHolder;

    @OneToOne
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
    public String getId() {
        return customerId;
    }

    public List<Dependent> listOfDependent() {
        // Create a Hibernate SessionFactory
//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<Dependent> dependentList = null;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            dependentList = session.createQuery("FROM Dependent", Dependent.class).getResultList();

            // Commit the transaction
            session.getTransaction().commit();
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            // Close the session and session factory
            session.close();
        }
        return dependentList;
    }


}


