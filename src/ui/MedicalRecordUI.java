package ui;

import java.util.Scanner;

import model.Patient;

public class MedicalRecordUI {

   public static void display(Patient patient, Scanner scanner) {
       // Define column widths
       String leftAlignFormat = "| %-15s | %-30s |%n";
       String separator = "+-----------------+--------------------------------+%n";

       // Print the header
       System.out.format("%n");
       System.out.format(separator);
       System.out.format("| PATIENT MEDICAL RECORD                           |%n");
       System.out.format(separator);
       
       // Basic Information
       System.out.format("| BASIC INFORMATION                                |%n");
       System.out.format(separator);
       System.out.format(leftAlignFormat, "Patient ID", patient.getId());
       System.out.format(leftAlignFormat, "Name", patient.getName());
       System.out.format(leftAlignFormat, "Date of Birth", patient.getDateOfBirth());
       System.out.format(leftAlignFormat, "Age", patient.getAge());
       System.out.format(leftAlignFormat, "Gender", patient.getGender());
       System.out.format(separator);

       // Contact Information
       System.out.format("| CONTACT INFORMATION                              |%n");
       System.out.format(separator);
       System.out.format(leftAlignFormat, "Phone", patient.getPhoneNumber());
       System.out.format(leftAlignFormat, "Email", patient.getEmail());
       System.out.format(separator);

       // Medical Information
       System.out.format("| MEDICAL INFORMATION                              |%n");
       System.out.format(separator);
       System.out.format(leftAlignFormat, "Blood Type", patient.getBloodType());
       System.out.format(separator);

       // Record Information
       System.out.format("| RECORD INFORMATION                               |%n");
       System.out.format(separator);
    //    System.out.format(leftAlignFormat, "Created At", patient.getCreatedAt());
    //    System.out.format(leftAlignFormat, "Updated At", patient.getUpdatedAt());
       System.out.format(separator);
       
       System.out.println("\nPress Enter to continue...");
       scanner.nextLine();
   }
}