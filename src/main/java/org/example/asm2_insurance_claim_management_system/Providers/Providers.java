package org.example.asm2_insurance_claim_management_system.Providers;
import jakarta.persistence.*;
@Entity
@Table(name = "Provider")
public abstract class Providers {
    @Id
    @Column(name = "ProviderId")
    private String providerId;

    @Column(name = "Password")
    private String password;

    @Column(name = "ProviderName")
    private String providerName;

    public String getPolicyOwnerId() {
        return providerId;
    }

    public void setPolicyOwnerId(String providerId) {
        this.providerId = providerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
