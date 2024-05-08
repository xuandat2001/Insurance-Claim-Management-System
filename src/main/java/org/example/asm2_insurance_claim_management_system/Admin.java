package org.example.asm2_insurance_claim_management_system;
import jakarta.persistence.*;
import java.io.Serializable;

    /**
     * @author COSC2440 Teaching Team
     * @version 1.0
     */

// NOTE: Remember to add the following dependency to pom.xml
//   <dependency>
//      <groupId>org.eclipse.persistence</groupId>
//      <artifactId>org.eclipse.persistence.jpa</artifactId>
//      <version>4.0.1</version>
//   </dependency>





    @Entity
    @Table(name = "Admin")
    public class Admin implements Serializable {
        @Id
        @Column(name = "AdminId")
        private String AdminId;
        @Column(name = "password")
        private String password;

        public Admin(String adminId, String password) {
            AdminId = adminId;
            this.password = password;
        }

        public Admin() {

        }

        public String getAdminId() {
            return AdminId;
        }

        public void setAdminId(String adminId) {
            AdminId = adminId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


