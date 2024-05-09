package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Interface.CRUDoperation;

@Entity
@Table(name = "PolicyOwner")
@PrimaryKeyJoinColumn(name = "PolicyOwnerId") // Discriminator value for PolicyOwner
public class PolicyOwner extends Customer implements CRUDoperation {


    @Column(name = "Location")
    private String location;

    public PolicyOwner(String policyOwnerId, String location) {

        this.location = location;
    }

    public PolicyOwner() {}



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean create() {
        return false;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean view() {
        return false;
    }
}
