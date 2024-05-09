package org.example.asm2_insurance_claim_management_system.Providers;
import jakarta.persistence.*;


@Entity
@Table(name = "Surveyor")
public class Surveyor {
    @Id
    @Column(name = "SurveyorID")
    private String surveyorID;
    private boolean isManager = true;

    public Surveyor(String surveyorID) {
        this.surveyorID = surveyorID;
    }

    public Surveyor() {

    }

    public String getSurveyorID() {
        return surveyorID;
    }

    public void setSurveyorID(String surveyorID) {
        this.surveyorID = surveyorID;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
