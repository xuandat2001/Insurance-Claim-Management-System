package org.example.asm2_insurance_claim_management_system.Admin;

import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Admin")
public class Admin implements Serializable, UserAuthentication {
    @Id
    @Column(name = "AdminId")
    private String AdminId;

    @Column(name = "password")
    private String password;

    // Private static instance variable to hold the single instance of Admin
    private static Admin instance;

    // Private constructor to prevent instantiation outside the class
    private Admin() {
    }


    // Public static method to get the single instance of Admin
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    // Other methods and fields remain unchanged...

    // Method to get the list of admins (may need modification to handle singleton instance)
    public List<Admin> listOfAdmin() {
        // Create a Hibernate SessionFactory
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        // Obtain a Hibernate Session
        Session session = sessionFactory.openSession();

        List<Admin> adminList = null;
        try {
            // Begin a transaction
            session.beginTransaction();

            // Perform a query
            adminList = session.createQuery("FROM Admin", Admin.class).getResultList();

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
        return adminList;
    }

    @Override
    public String getId() {
        return AdminId;
    }

    @Override
    public String getPassword() {
        return password;
    }


    // Other methods and fields remain unchanged...
}