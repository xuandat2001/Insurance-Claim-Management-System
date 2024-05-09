package org.example.asm2_insurance_claim_management_system.Providers;

import jakarta.persistence.*;


@Entity
@Table(name = "Manager")
public class Manager {
    @Id
    @Column(name = "ManagerID")
    private String managerID;

    private boolean isManager = true;

    public Manager(String managerID) {
        this.managerID = managerID;
    }

    public Manager() {

    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }
}
