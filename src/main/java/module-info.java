module org.example.asm2_insurance_claim_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens org.example.asm2_insurance_claim_management_system to javafx.fxml;
    exports org.example.asm2_insurance_claim_management_system;
}