package org.example.asm2_insurance_claim_management_system.Admin;

import org.example.asm2_insurance_claim_management_system.Customers.PolicyHolder;
import org.example.asm2_insurance_claim_management_system.Login.Authentication;


import java.util.List;
import java.util.Scanner;

public class PersistenceApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String userName = scanner.nextLine();
        System.out.println("Enter your password : ");
        String password = scanner.nextLine();

        Authentication login = new Authentication();
        Admin admin = Admin.getInstance();
        if (login.authenticate(admin.listOfAdmin(),userName,password ) != null){
            System.out.println("Login successfully");
            PolicyHolder policyHolder = new PolicyHolder();
            policyHolder.create();
        }
        else {
            System.out.println("Username and Password are not correct");
        }
    }
}

