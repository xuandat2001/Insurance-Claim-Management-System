package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
@Entity
@DiscriminatorValue("Dependent") // Discriminator value for Dependent
public class Dependent extends Customer{
    @Id
    @Column(name = "DependentId")
    private String dependentId;

    @Column(name = "PolicyHolderId")
    private String policyHolderId;
    @Column(name = "PolicyOwnerId")
    private String policyOwnerId;

    public Dependent(String dependentId, String policyHolderId, String policyOwnerId) {
        this.dependentId = dependentId;
        this.policyHolderId = policyHolderId;
        this.policyOwnerId = policyOwnerId;
    }

    public Dependent() {

    }

    public String getDependentId() {
        return dependentId;
    }

    public void setDependentId(String dependentId) {
        this.dependentId = dependentId;
    }

    public String getPolicyHolderId() {
        return policyHolderId;
    }

    public void setPolicyHolderId(String policyHolderId) {
        this.policyHolderId = policyHolderId;
    }

    public String getPolicyOwnerId() {
        return policyOwnerId;
    }

    public void setPolicyOwnerId(String policyOwnerId) {
        this.policyOwnerId = policyOwnerId;
    }
}
