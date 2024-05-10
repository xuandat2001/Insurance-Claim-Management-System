package org.example.asm2_insurance_claim_management_system.Claim;
import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.InsuranceCard.InsuranceCard;

import java.time.LocalDate;



@Entity
@Table(name = "Claim")
public class Claim {
    @Id
    @Column(name = "ClaimId")
    private String claimId;

    @Column(name = "ClaimDate")
    private LocalDate claimDate;

    @Column(name = "ExamDate")
    private LocalDate examDate;

    @Column(name = "ListOfDocument")
    private String listOfDocument;

    @Column(name = "Status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "CardNumber") // foreign key referencing InsuranceCard primary key
    private InsuranceCard insuranceCard;

    @ManyToOne
    @JoinColumn(name = "PolicyHolderId") // foreign key referencing PolicyHolder primary key
    private PolicyHolder policyHolder;

    @ManyToOne
    @JoinColumn(name = "DependentId") // foreign key referencing Dependent primary key
    private Dependent dependent;

    // Constructors, getters, and setters

    public Claim() {
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getListOfDocument() {
        return listOfDocument;
    }

    public void setListOfDocument(String listOfDocument) {
        this.listOfDocument = listOfDocument;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    public Dependent getDependent() {
        return dependent;
    }

    public void setDependent(Dependent dependent) {
        this.dependent = dependent;
    }
}
