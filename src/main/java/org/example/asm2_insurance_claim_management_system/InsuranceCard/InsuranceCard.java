package org.example.asm2_insurance_claim_management_system.InsuranceCard;

import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;

import java.time.LocalDate;

@Entity
@Table(name = "InsuranceCard")
public class InsuranceCard {
    @Id
    @Column(name = "CardNumber")
    private String cardNumber;
    @Column(name = "expirationDate")
    private LocalDate expirationDate;

    @Column(name = "PolicyOwnerId")
    private String policyOwnerId;
    @Column(name = "CardHolder")
    private String cardHolder;
    @OneToOne(mappedBy = "insuranceCard")
    private PolicyHolder policyHolder;
    public InsuranceCard(String cardNumber, LocalDate expirationDate, String policyOwnerId, String cardHolder) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.policyOwnerId = policyOwnerId;
        this.cardHolder = cardHolder;
    }

    public InsuranceCard() {

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPolicyOwnerId() {
        return policyOwnerId;
    }

    public void setPolicyOwnerId(String policyOwnerId) {
        this.policyOwnerId = policyOwnerId;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
}
