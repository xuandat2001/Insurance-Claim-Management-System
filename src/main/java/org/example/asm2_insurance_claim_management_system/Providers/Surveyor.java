package org.example.asm2_insurance_claim_management_system.Providers;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

@Entity
@Table(name = "Surveyor")
@PrimaryKeyJoinColumn(name = "SurveyorID")
public class Surveyor extends Providers {
    public Surveyor() {}

    public List<Surveyor> ListOfSurveyor(){
        // Create a Hibernate SessionFactory

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<Surveyor> surveyorList = null;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            surveyorList = session.createQuery("FROM Surveyor", Surveyor.class).getResultList();

            // Commit the transaction
            session.getTransaction().commit();
        } catch (Exception ex) {
            // Rollback the transaction in case of an exception
            session.getTransaction().rollback();
            ex.printStackTrace();
        }
        return surveyorList;
    }
}
