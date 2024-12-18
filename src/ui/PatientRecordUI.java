package ui;

import enums.MedicalRecordType;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import model.MedicalRecord;
import model.MedicalRecordDetail;
import model.Patient;
import model.PatientVital;

/**
 * The PatientRecordUI class provides a user interface for displaying a patient's medical record.
 * It organizes and presents various details such as personal information, contact details, vital signs, 
 * diagnoses, and treatments in a formatted structure.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public class PatientRecordUI {

    /**
     * Displays the patient's medical record.
     * This includes personal details, contact information, vital signs, diagnoses, and treatments. 
     * The data is presented in a structured and formatted manner for easy readability.
     *
     * @param patient the Patient object containing personal and medical information
     * @param scanner the Scanner object used to capture user input
     * @param records an array of MedicalRecordDetail objects representing the patient's medical history
     * @param vital   the PatientVital object containing the patient's vital signs
     */
    public static void display(Patient patient, Scanner scanner, MedicalRecordDetail[] records, PatientVital vital) {
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
        System.out.format(leftAlignFormat, "Phone", (patient.getPhoneNumber() == 0) ? "No phone number" : patient.getPhoneNumber());
        System.out.format(leftAlignFormat, "Email", patient.getEmail());
        System.out.format(separator);

        // Medical Information
        System.out.format(headerFormat, "MEDICAL INFORMATION");
        System.out.format(separator);
        System.out.format(leftAlignFormat, "Blood Type", patient.getBloodType());
        System.out.format(separator);

        // Vital Signs (if available)
        System.out.format(headerFormat, "VITAL SIGNS");
        System.out.format(separator);
        if (vital != null) {
            System.out.format(leftAlignFormat, "Blood Oxygen", vital.getBloodOxygen() + "%");
            System.out.format(leftAlignFormat, "Height", vital.getHeight() + " cm");
            System.out.format(leftAlignFormat, "Weight", vital.getWeight() + " kg");
            System.out.format(leftAlignFormat, "Blood Pressure", vital.getBloodPressure() + " mmHg");
            
            // Calculate and display BMI
            double heightInMeters = vital.getHeight() / 100.0;
            double bmi = vital.getWeight() / (heightInMeters * heightInMeters);
            System.out.format(leftAlignFormat, "BMI", String.format("%.1f", bmi));
            System.out.format(leftAlignFormat, "Last Updated", vital.getUpdatedAt().format(dateFormat));
        } else {
            System.out.format(leftAlignFormat, "Status", "No vital signs recorded");
        }
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
