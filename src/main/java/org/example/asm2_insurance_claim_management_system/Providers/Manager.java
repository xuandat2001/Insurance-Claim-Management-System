package org.example.asm2_insurance_claim_management_system.Providers;
/**
 * @author <Group 22>
 */
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

@Entity
@Table(name = "Manager")
@PrimaryKeyJoinColumn(name = "ManagerID")
public class Manager extends Providers {

    public Manager() {}

    public List<Manager> ListOfManager(){
        // Create a Hibernate SessionFactory

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

        return ListOfManager;
    }
}
