package org.example.asm2_insurance_claim_management_system;
import jakarta.persistence.*;

import java.util.List;
public class PersistenceApp {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();


        entityManager.getTransaction().begin();
        Query q = entityManager.createQuery("SELECT a "
                + "FROM Admin a ");
        List<Admin> AdminList = q.getResultList();
        for (Admin a : AdminList) {
            System.out.println(a.getAdminId()
                    + "\t" + a.getPassword()
                    );
        }
        entityManager.getTransaction().commit();

    }
}
