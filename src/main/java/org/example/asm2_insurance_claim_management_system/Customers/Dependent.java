package org.example.asm2_insurance_claim_management_system.Customers;
import jakarta.persistence.*;
@Entity
@Table(name = "Dependent")
public class Dependent extends customer{
    @Id
    @Column(name = "DependentId")
    private String dependentId;

    @Column(name = "PolicyHolderId")
    private String policyHolderId;
    @Column(name = "PolicyOwnerId")
    private String policyOwnerId;

}
