package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
@Entity
@Table(name = "PolicyHolder")
public class PolicyHolder extends customer {
    @Id
    @Column(name = "PolicyHolderId")
    private String policyHolderId;

    @Column(name = "insuranceCardNumber")
    private String insuranceCardNumber;

    @Column(name = "PolicyOwnerId")
    private String policyOwnerId;

    public PolicyHolder(String policyHolderId, String insuranceCardNumber, String policyOwnerId) {
        this.policyHolderId = policyHolderId;
        this.insuranceCardNumber = insuranceCardNumber;
        this.policyOwnerId = policyOwnerId;
    }

    public PolicyHolder() {}

    public String getPolicyHolderId() {
        return policyHolderId;
    }

    public void setPolicyHolderId(String policyHolderId) {
        this.policyHolderId = policyHolderId;
    }

    public String getInsuranceCardNumber() {
        return insuranceCardNumber;
    }

    public void setInsuranceCardNumber(String insuranceCardNumber) {
        this.insuranceCardNumber = insuranceCardNumber;
    }

    public String getPolicyOwnerId() {
        return policyOwnerId;
    }

    public void setPolicyOwnerId(String policyOwnerId) {
        this.policyOwnerId = policyOwnerId;
    }
}
