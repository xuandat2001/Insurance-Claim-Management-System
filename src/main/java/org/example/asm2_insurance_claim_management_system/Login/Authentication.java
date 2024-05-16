package org.example.asm2_insurance_claim_management_system.Login;

import org.example.asm2_insurance_claim_management_system.Admin.Admin;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;

import java.security.PublicKey;
import java.util.List;
import java.util.Objects;

public class Authentication {
    public Object authenticate(String userName, String password){
        Admin admin = Admin.getInstance();

            for (Admin adminAuthentication : admin.listOfAdmin()) {
                if (adminAuthentication.getId().equals(userName) && adminAuthentication.getPassword().equals(password)){
                    //Authentication success
                    return adminAuthentication;
                }
            }
        PolicyHolder policyHolder = new PolicyHolder();
        for (PolicyHolder policyHolderAuthentication : policyHolder.listOfPolicyHolder()) {
            if (policyHolderAuthentication.getId().equals(userName) && policyHolderAuthentication.getPassword().equals(password)){
                //Authentication success
                return policyHolderAuthentication;
            }
        }

        //Authentication fail
            return null;
    }


}
