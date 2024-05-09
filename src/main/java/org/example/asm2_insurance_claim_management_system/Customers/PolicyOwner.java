package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
@Entity
@Table(name = "PolicyOwner")
public class PolicyOwner extends customer {
    @Id
    @Column(name = "PolicyOwnerId")
    private String policyOwnerId;

    @Column(name = "Location")
    private String location;

    public PolicyOwner(String policyOwnerId, String location) {
        this.policyOwnerId = policyOwnerId;
        this.location = location;
    }

    public PolicyOwner() {}

    public String getPolicyOwnerId() {
        return policyOwnerId;
    }

    public void setPolicyOwnerId(String policyOwnerId) {
        this.policyOwnerId = policyOwnerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
