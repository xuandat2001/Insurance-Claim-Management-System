package org.example.asm2_insurance_claim_management_system.Login;
/**
 * @author <Group 22>
 */
import org.example.asm2_insurance_claim_management_system.Admin.Admin;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.Interface.UserAuthentication;
import org.example.asm2_insurance_claim_management_system.Providers.Manager;
import org.example.asm2_insurance_claim_management_system.Providers.Surveyor;

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
        PolicyOwner policyOwner = new PolicyOwner();
        for (PolicyOwner policyOwnerAuthentication : policyOwner.listOfPolicyOwner()) {
            if (policyOwnerAuthentication .getId().equals(userName) && policyOwnerAuthentication .getPassword().equals(password)){
                //Authentication success
                return policyOwnerAuthentication ;
            }
        }
       Dependent dependent = new Dependent();
        for (Dependent dependentAuthentication : dependent.listOfDependent()) {
            if (dependentAuthentication .getId().equals(userName) && dependentAuthentication .getPassword().equals(password)){
                //Authentication success
                return dependentAuthentication ;
            }
        }
        Surveyor surveyor = new Surveyor();
        for (Surveyor surveyorAuthentication : surveyor.ListOfSurveyor()) {
            if (surveyorAuthentication .getProviderId().equals(userName) && surveyorAuthentication .getPassword().equals(password)){
                //Authentication success
                return surveyorAuthentication ;
            }
        }
        Manager manger = new Manager();
        for (Manager managerAuthentication : manger.ListOfManager()) {
            if (managerAuthentication .getProviderId().equals(userName) && managerAuthentication .getPassword().equals(password)){
                //Authentication success
                return managerAuthentication ;
            }
        }

        //Authentication fail
            return null;
    }


}
