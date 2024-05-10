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
    private PolicyHolder policyHolder;
    private PolicyOwner policyOwner;
    private Dependent dependent;
    public UserAuthentication authenticate(List<? extends UserAuthentication>listOfUser, String userName, String password){
            for (UserAuthentication userAuthentication : listOfUser) {
                if (userAuthentication.getId().equals(userName) && userAuthentication.getPassword().equals(password)){
                    //Authentication success
                    return userAuthentication;
                }
            }
        //Authentication fail
            return null;
    }

}
