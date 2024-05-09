package org.example.asm2_insurance_claim_management_system.InsuranceCard;

import jakarta.persistence.*;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;

import java.time.LocalDate;

@Entity
@Table(name = "InsuranceCard")
public class InsuranceCard {
    @Id
    @Column(name = "CardNumber")
    private String cardNumber;
    @Column(name = "expirationDate")
    private LocalDate expirationDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PolicyOwnerId") // foreign key referencing InsuranceCard's primary key
    private PolicyOwner policyOwner;
    @Column(name = "CardHolder")
    private String cardHolder;



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



    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
}
