package org.example.asm2_insurance_claim_management_system.Claim;
import jakarta.persistence.*;


@Entity
@Table(name = "Claim")
public class BankInfo {
    @Id
    @Column(name = "BankId")
    private String bankID;

    @Column(name = "BankName")
    private String bankName;

    @Column(name = "OwnerName")
    private String OwnerName;

    @Column(name = "AccountNumber")
    private String accountNumber;


    public BankInfo() {
    }

    public String getBankID() {
        return bankID;
    }

    public String getBankName() {
        return bankName;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}
