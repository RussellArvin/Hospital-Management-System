package ui;

import enums.BloodType;
import enums.Gender;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import service.PatientService;

/**
 * The RegisterPatientUI class provides the user interface for registering a new patient.
 * It collects information such as name, date of birth, gender, blood type, phone number, and email
 * and creates a new patient record using the PatientService.
 * 
 * @author Tan Jou Yuan
 * @version 1.0
 */
public class RegisterPatientUI {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Displays the user interface for registering a new patient.
     * Prompts the user for patient details, validates the inputs, and creates a new patient record.
     *
     * @param scanner        the Scanner object used to read user input
     * @param patientService the service used to manage patient-related actions
     */
    public static void display(Scanner scanner, PatientService patientService) {
        // Define formatting
        String leftAlignFormat = "| %-30s |%n";
        String separator = "+--------------------------------+%n";

        // Print the header
        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| REGISTER NEW PATIENT              |%n");
        System.out.format(separator);

        // Get patient name
        System.out.format(leftAlignFormat, "Enter Patient Name:");
        String name = scanner.nextLine().trim();

        // Get date of birth
        LocalDate dateOfBirth = null;
        while (dateOfBirth == null) {
            System.out.format(leftAlignFormat, "Enter Date of Birth:");
            System.out.format(leftAlignFormat, "(Format: YYYY-MM-DD)");
            String dobStr = scanner.nextLine().trim();

            try {
                dateOfBirth = LocalDate.parse(dobStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.format(leftAlignFormat, "Invalid date format!");
                System.out.format(separator);
                continue;
            }
        }

        // Get gender
        Gender gender = null;
        while (gender == null) {
            System.out.format(leftAlignFormat, "Enter Gender (M/F):");
            String genderStr = scanner.nextLine().trim().toUpperCase();

            try {
                gender = Gender.valueOf(genderStr.equals("M") ? "MALE" : genderStr.equals("F") ? "FEMALE" : "INVALID");
                if (gender.toString().equals("INVALID")) {
                    gender = null;
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.out.format(leftAlignFormat, "Invalid gender! Enter M or F");
                System.out.format(separator);
                continue;
            }
        }

        // Get blood type
        BloodType bloodType = null;
        while (bloodType == null) {
            System.out.format(leftAlignFormat, "Enter Blood Type:");
            System.out.format(leftAlignFormat, "(A+,A-,B+,B-,AB+,AB-,O+,O-)");
            String bloodTypeStr = scanner.nextLine().trim().toUpperCase();

            try {
                bloodType = BloodType.valueOf(bloodTypeStr.replace("+", "_POSITIVE").replace("-", "_NEGATIVE"));
            } catch (IllegalArgumentException e) {
                System.out.format(leftAlignFormat, "Invalid blood type!");
                System.out.format(separator);
                continue;
            }
        }

        // Get phone number
        int phoneNumber = 0;
        while (phoneNumber == 0) {
            System.out.format(leftAlignFormat, "Enter Phone Number:");
            String phoneStr = scanner.nextLine().trim();

            try {
                phoneNumber = Integer.parseInt(phoneStr);
                if (phoneNumber <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.format(leftAlignFormat, "Invalid phone number!");
                System.out.format(separator);
                continue;
            }
        }

        // Get email
        System.out.format(leftAlignFormat, "Enter Email:");
        String email = scanner.nextLine().trim();

        // Create the patient
        String result = patientService.create(name, dateOfBirth, gender, bloodType, phoneNumber, email);

        System.out.format(separator);
        if (result == null) {
            System.out.format(leftAlignFormat, "Patient registered successfully!");
        } else {
            System.out.format(leftAlignFormat, "Error: " + result);
        }
        System.out.format(separator);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
