package org.example.asm2_insurance_claim_management_system.Customers;

import jakarta.persistence.*;
@Entity
@Table(name = "customers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "customer_type")
public abstract class  Customer {
    @Id
    @Column(name = "CustomerId")
    private String customerId;
    @Column(name = "password")
    private String password;
    @Column(name = "fullName")
    private String fullName;

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
