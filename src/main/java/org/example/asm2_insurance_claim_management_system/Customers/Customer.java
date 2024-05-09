package org.example.asm2_insurance_claim_management_system.Customers;

import jakarta.persistence.*;
@Entity
@Table(name = "Customer")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class  Customer {
    @Id
    @Column(name = "CustomerId")
    String customerId;
    @Column(name = "password")
    String password;
    @Column(name = "fullName")
    String fullName;



    public Customer() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
