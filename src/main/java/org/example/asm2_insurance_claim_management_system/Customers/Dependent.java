package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;

@Entity
@Table(name = "Dependent")
@PrimaryKeyJoinColumn(name = "DependentId")// Discriminator value for Dependent
public class Dependent extends Customer {


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PolicyHolderId") // foreign key referencing InsuranceCard's primary key
    private PolicyHolder policyHolder;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PolicyOwnerId") // foreign key referencing InsuranceCard's primary key
    private PolicyOwner policyOwner;
    public Dependent() {

    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    public PolicyOwner getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(PolicyOwner policyOwner) {
        this.policyOwner = policyOwner;
    }
}


