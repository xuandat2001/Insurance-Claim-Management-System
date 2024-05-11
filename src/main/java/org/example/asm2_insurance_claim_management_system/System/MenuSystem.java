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
        Admin admin = Admin.getInstance();
        PolicyHolder policyHolder = new PolicyHolder();
        PolicyOwner policyOwner = new PolicyOwner();
        Dependent dependent = new Dependent();
        if (login.authenticate(admin.listOfAdmin(), userName, password) != null) {
            System.out.println("Login successfully (Admin)");
            policyHolder.create();
//            policyHolder.update();
//            policyHolder.delete();
            //policyHolder.view();
//            Dependent dependent = new Dependent();
//            policyHolder.retrieveClaim();
//            policyHolder.createClaim();
//            dependent.create();
//            Claim claim = new Claim();
//            claim.createClaim();
//            claim.updateClaim();
        } else if (login.authenticate(policyHolder.listOfPolicyHolder(), userName, password) != null) {
            System.out.println("Login successfully (PolicyHolder)");
            PolicyHolder authenticatedPolicyHolder = (PolicyHolder) login.authenticate(policyHolder.listOfPolicyHolder(), userName, password);
            authenticatedPolicyHolder.retrieveClaim();
//            policyHolder.createClaim();
        } else if (login.authenticate(policyOwner.listOfPolicyOwner(), userName, password) != null) {
            System.out.println("Login successfully (PolicyOwner)");
            PolicyOwner authenticatedPolicyOwner = (PolicyOwner) login.authenticate(policyOwner.listOfPolicyOwner(), userName, password);

        } else if (login.authenticate(dependent.listOfDependent(), userName, password) != null) {
            System.out.println("Login successfully (Dependent)");
            Dependent authenticatedDependent = (Dependent) login.authenticate(dependent.listOfDependent(), userName, password);

        } else {
            System.out.println("Username and Password are not correct");
        }
    }
}

