package org.example.asm2_insurance_claim_management_system.System;

import org.example.asm2_insurance_claim_management_system.Admin.Admin;
import org.example.asm2_insurance_claim_management_system.Customers.Dependent;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Customers.PolicyOwner;
import org.example.asm2_insurance_claim_management_system.Login.Authentication;

import java.util.Scanner;

public class MenuSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String userName = scanner.nextLine();
        System.out.println("Enter your password : ");
        String password = scanner.nextLine();

        Authentication login = new Authentication();
        Object user = login.authenticate(userName, password);
        Admin admin = Admin.getInstance();

        if (user instanceof Admin) {
            System.out.println("Login successfully (Admin)");
//            policyHolder.create();
//            policyHolder.update();
//            policyHolder.delete();
            //policyHolder.view();
//            Dependent dependent = new Dependent();
//            policyHolder.retrieveClaim();
//            policyHolder.createClaim();
//            dependent.create();
//            dependent.delete();
//            policyOwner.create();
//            Claim claim = new Claim();
//            claim.createClaim();
//            claim.updateClaim();
        } else if (user instanceof PolicyHolder) {
            System.out.println("Login successfully (PolicyHolder)");
            PolicyHolder authenticatedPolicyHolder = (PolicyHolder) login.authenticate( userName, password);
//            authenticatedPolicyHolder.filePolicyHolderClaim();
//            authenticatedPolicyHolder.retrievePolicyHolderClaim();
//            authenticatedPolicyHolder.updatePolicyHolderClaim();
//            authenticatedPolicyHolder.showPolicyHolderInfo();
//            authenticatedPolicyHolder.getAllDependent();
 //          authenticatedPolicyHolder.updatePolicyHolderInfo();
//            authenticatedPolicyHolder.fileClaimForDependent();
//            authenticatedPolicyHolder.updateClaimForDependent();
//            authenticatedPolicyHolder.retrieveClaimForDependent();
//            authenticatedPolicyHolder.updateInfoForDependent();

        } else if (user instanceof PolicyOwner) {
            System.out.println("Login successfully (PolicyOwner)");
            PolicyOwner authenticatedPolicyOwner = (PolicyOwner) login.authenticate( userName, password);
//            authenticatedPolicyOwner.filePolicyHolderClaim();
//            authenticatedPolicyOwner.updatePolicyHolderClaim();
//            authenticatedPolicyOwner.retrievePolicyHolderClaim();
//            authenticatedPolicyOwner.updatePolicyHolderInfo();
//            authenticatedPolicyOwner.showPolicyHolderInfo();
//            authenticatedPolicyOwner.fileClaimForDependent();
//            authenticatedPolicyOwner.updateClaimForDependent();
//            authenticatedPolicyOwner.retrieveClaimForDependent();
//            authenticatedPolicyOwner.updateInfoForDependent();
//            authenticatedPolicyOwner.getAllDependent();
//            authenticatedPolicyOwner.deletePolicyHolder();
//            authenticatedPolicyOwner.deleteDependent();
//            authenticatedPolicyOwner.addPolicyHolder();
//            authenticatedPolicyOwner.addDependentForPolicyHolder();
//            authenticatedPolicyOwner.getNumberOfPolicyHolder();
//            authenticatedPolicyOwner.calcInsuranceFee();


        } else if (user instanceof Dependent) {
            System.out.println("Login successfully (Dependent)");
            Dependent authenticatedDependent = (Dependent) login.authenticate(userName, password);
//            authenticatedDependent.showInfo();
//            authenticatedDependent.retrieveClaim();
        } else {
            System.out.println("Username and Password are not correct");
        }
    }
}

