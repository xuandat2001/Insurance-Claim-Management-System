package org.example.asm2_insurance_claim_management_system.Login;

import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;

import java.util.List;

public class Authentication {
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
