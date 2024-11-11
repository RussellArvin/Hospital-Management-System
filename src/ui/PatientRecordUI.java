package ui;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import model.MedicalRecord;
import model.MedicalRecordDetail;
import model.Patient;
import enums.MedicalRecordType;

public class PatientRecordUI {
    public static void display(Patient patient, Scanner scanner, MedicalRecordDetail[] records) {
        // Define column widths and formats
        String leftAlignFormat = "| %-15s | %-30s |%n";
        String headerFormat = "| %-47s |%n";
        String separator = "+-----------------+--------------------------------+%n";

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Print the header
        System.out.format("%n");
        System.out.format(separator);
        System.out.format(headerFormat, "PATIENT MEDICAL RECORD");
        System.out.format(separator);
        
        // Basic Information
        System.out.format(headerFormat, "BASIC INFORMATION");
        System.out.format(separator);
        System.out.format(leftAlignFormat, "Patient ID", patient.getId());
        System.out.format(leftAlignFormat, "Name", patient.getName());
        System.out.format(leftAlignFormat, "Date of Birth", patient.getDateOfBirth());
        System.out.format(leftAlignFormat, "Age", patient.getAge());
        System.out.format(leftAlignFormat, "Gender", patient.getGender());
        System.out.format(separator);

        // Contact Information
        System.out.format(headerFormat, "CONTACT INFORMATION");
        System.out.format(separator);
        System.out.format(leftAlignFormat, "Phone", patient.getPhoneNumber());
        System.out.format(leftAlignFormat, "Email", patient.getEmail());
        System.out.format(separator);

        // Medical Information
        System.out.format(headerFormat, "MEDICAL INFORMATION");
        System.out.format(separator);
        System.out.format(leftAlignFormat, "Blood Type", patient.getBloodType());
        System.out.format(separator);

        // Filter records by type
        MedicalRecordDetail[] diagnoses = Arrays.stream(records)
            .filter(record -> record.getType() == MedicalRecordType.DIAGNOSIS)
            .toArray(MedicalRecordDetail[]::new);

        MedicalRecordDetail[] treatments = Arrays.stream(records)
            .filter(record -> record.getType() == MedicalRecordType.TREATMENT)
            .toArray(MedicalRecordDetail[]::new);

        // Display Diagnoses
        System.out.format(headerFormat, "DIAGNOSES");
        System.out.format(separator);
        if (diagnoses.length == 0) {
            System.out.format(leftAlignFormat, "Status", "No diagnoses recorded");
        } else {
            for (MedicalRecordDetail diagnosis : diagnoses) {
                System.out.format(leftAlignFormat, "Details", diagnosis.getDetails());
                System.out.format(leftAlignFormat, "Date", diagnosis.getCreatedAt().format(dateFormat));
                System.out.format(leftAlignFormat, "Doctor", diagnosis.getDoctor().getName());
                System.out.format(separator);
            }
        }
        System.out.format(separator);

        // Display Treatments
        System.out.format(headerFormat, "TREATMENTS");
        System.out.format(separator);
        if (treatments.length == 0) {
            System.out.format(leftAlignFormat, "Status", "No treatments recorded");
        } else {
            for (MedicalRecord treatment : treatments) {
                System.out.format(leftAlignFormat, "Details", treatment.getDetails());
                System.out.format(leftAlignFormat, "Date", treatment.getCreatedAt().format(dateFormat));
                System.out.format(separator);
            }
        }
        System.out.format(separator);
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}