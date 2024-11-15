package ui;

import java.util.Scanner;
import model.Patient;
import service.PatientService;

/**
 * The CreatePatientVitalUI class provides a user interface for creating a new vital record for a patient.
 * It allows the user to input patient details and vital sign measurements, validates the inputs, and saves the record.
 * 
 * @author Lim Jun Howe 
 * @version 1.0 
 */
public class CreatePatientVitalUI {
    private final Scanner scanner;
    private final PatientService patientService;
    private final String leftAlignFormat = "| %-30s |%n";
    private final String separator = "+--------------------------------+%n";

    /**
     * Constructs a CreatePatientVitalUI instance with the specified scanner and patient service.
     *
     * @param scanner        the Scanner object used to read user input
     * @param patientService the service used to manage patient-related actions
     */
    public CreatePatientVitalUI(Scanner scanner, PatientService patientService) {
        this.scanner = scanner;
        this.patientService = patientService;
    }

    /**
     * Displays the user interface for creating a new vital record.
     * Prompts the user to enter patient details, validate the patient, and input vital sign measurements.
     * The vital record is then saved using the patient service.
     */
    public void display() {
        // Print the header
        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| CREATE NEW VITAL RECORD           |%n");
        System.out.format(separator);

        // Get patient and verify
        Patient patient = null;
        while (patient == null) {
            System.out.format(leftAlignFormat, "Enter Patient Name:");
            String patientName = scanner.nextLine().trim();

            patient = patientService.getPatientByName(patientName);
            if (patient == null) {
                System.out.format(leftAlignFormat, "Patient not found! Try again.");
                System.out.format(separator);
                continue;
            }
        }

        // Display patient information
        System.out.format("| PATIENT DETAILS                    |%n");
        System.out.format(separator);
        System.out.format(leftAlignFormat, "Patient: " + patient.getName());
        System.out.format(leftAlignFormat, "ID: " + patient.getId());
        System.out.format(separator);

        // Get vital signs
        int bloodOxygen = getNumericInput("Blood Oxygen (%)", 0, 100);
        int height = getNumericInput("Height (cm)", 0, 300);
        int weight = getNumericInput("Weight (kg)", 0, 500);
        int bloodPressure = getNumericInput("Blood Pressure (mmHg)", 0, 300);

        // Save the vital record
        String result = patientService.createPatientVital(
            patient.getId(),
            bloodOxygen,
            height,
            weight,
            bloodPressure
        );

        System.out.format(separator);
        if (result == null) {
            System.out.format(leftAlignFormat, "Vital record created successfully!");
        } else {
            System.out.format(leftAlignFormat, "Error: " + result);
        }
        System.out.format(separator);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Prompts the user to enter a numeric value for a specified vital sign.
     * Ensures the input is a valid number within the given range.
     *
     * @param prompt the prompt text to display to the user
     * @param min    the minimum acceptable value
     * @param max    the maximum acceptable value
     * @return the valid numeric input entered by the user
     */
    private int getNumericInput(String prompt, int min, int max) {
        while (true) {
            System.out.format(leftAlignFormat, "Enter " + prompt + ":");
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);

                if (value < min || value > max) {
                    System.out.format(leftAlignFormat,
                        "Value must be between " + min + " and " + max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.format(leftAlignFormat, "Please enter a valid number");
            }
        }
    }
}
