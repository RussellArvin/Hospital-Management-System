package main;

import java.util.Scanner; 

import repository.PatientRepository;
import service.UserService;
import model.Patient;
import model.User;


public class Main {
    public static void main(String[] args) {
        // Initialize repositories and services
        PatientRepository patientRepository = new PatientRepository();
        UserService userService = new UserService(patientRepository);
        
        // Create scanner for user input
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Hospital Management System ===");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                // Get login credentials
                System.out.print("Enter ID: ");
                String id = scanner.nextLine();

                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                // Try to login
                User user = userService.Login(id, password);

                if (user != null) {
                    System.out.println("\nLogin successful!");
                    System.out.println("Welcome, " + user.getName());
                    // Here you could add more menu options for logged-in user
                } else {
                    System.out.println("\nLogin failed. Invalid ID or password.");
                }
            } 
            else if (choice.equals("2")) {
                System.out.println("Thank you for using the Hospital Management System.");
                break;
            } 
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}