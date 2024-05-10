module org.example.asm2_insurance_claim_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens org.example.asm2_insurance_claim_management_system to javafx.fxml, org.hibernate.orm.core;
    exports org.example.asm2_insurance_claim_management_system;
    exports org.example.asm2_insurance_claim_management_system.Admin;
    opens org.example.asm2_insurance_claim_management_system.Admin to javafx.fxml, org.hibernate.orm.core;
    exports org.example.asm2_insurance_claim_management_system.Login;
    opens org.example.asm2_insurance_claim_management_system.Login to javafx.fxml, org.hibernate.orm.core;
    exports org.example.asm2_insurance_claim_management_system.Interface;
    opens org.example.asm2_insurance_claim_management_system.Interface to javafx.fxml, org.hibernate.orm.core;
    exports org.example.asm2_insurance_claim_management_system.Customers;
    opens org.example.asm2_insurance_claim_management_system.Customers to javafx.fxml, org.hibernate.orm.core;
    exports org.example.asm2_insurance_claim_management_system.Providers;
    opens org.example.asm2_insurance_claim_management_system.Providers to javafx.fxml, org.hibernate.orm.core;
    exports org.example.asm2_insurance_claim_management_system.InsuranceCard;
    opens org.example.asm2_insurance_claim_management_system.InsuranceCard to javafx.fxml, org.hibernate.orm.core;
    exports org.example.asm2_insurance_claim_management_system.Claim;
    opens org.example.asm2_insurance_claim_management_system.Claim to javafx.fxml, org.hibernate.orm.core;
}