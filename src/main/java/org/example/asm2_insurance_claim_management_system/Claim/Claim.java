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

    @Column(name = "ClaimAmount")
    private double claimAmount;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "CardNumber") // foreign key referencing InsuranceCard primary key
    private InsuranceCard insuranceCard;

    @ManyToOne
    @JoinColumn(name = "PolicyHolder") // foreign key referencing PolicyHolder primary key
    private PolicyHolder policyHolder;

    @ManyToOne
    @JoinColumn(name = "Dependent") // foreign key referencing Dependent primary key
    private Dependent dependent;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "BankInfo")
    private BankInfo bankInfo;

    @Column(name = "RequiredInfo")
    private String requiredinfo;

    @Column(name = "Approval")
    private String approval;

    // Constructors, getters, and setters

    public Claim() {
    }

    public String getRequiredinfo() {
        return requiredinfo;
    }

    public void setRequiredinfo(String requiredinfo) {
        this.requiredinfo = requiredinfo;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
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

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }

    @Override
    public String toString() {
        return "ClaimId = " + claimId + '\n' +
                "claimDate = " + claimDate +
                ", listOfDocument = " + listOfDocument +
                ", claimAmount = " + claimAmount + '\n' +
                "insuranceCard = " + insuranceCard.getCardNumber() +
                ", policyHolder = " + policyHolder.getFullName() +
                ", dependent = " + dependent.getFullName() +
                ", bankInfo = " + bankInfo.getBankID() + "\n";
    }

    public void showInfo(){
        if (this.getDependent() == null){
            System.out.println("ClaimId = " + claimId + '\n' +
                    "claimDate = " + claimDate +
                    ", listOfDocument = " + listOfDocument +
                    ", claimAmount = " + claimAmount + '\n' +
                    "insuranceCard = " + insuranceCard.getCardNumber() +
                    ", policyHolder = " + policyHolder.getFullName() +
                    ", dependent = null" +
                    ", bankInfo = " + bankInfo.getBankID() + "\n");
        }
        else {
            System.out.println("ClaimId = " + claimId + '\n' +
                    "claimDate = " + claimDate +
                    ", listOfDocument = " + listOfDocument +
                    ", claimAmount = " + claimAmount + '\n' +
                    "insuranceCard = " + insuranceCard.getCardNumber() +
                    ", policyHolder = " + policyHolder.getFullName() +
                    ", dependent = " + dependent.getFullName() +
                    ", bankInfo = " + bankInfo.getBankID() + "\n");
        }
    }
}
