package ui;

import enums.MedicalRecordType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import model.MedicalRecordDetail;
import model.Patient;
import model.PatientVital;
import service.PatientService;

/**
 * The PatientTableUI class provides a user interface for displaying and managing a list of patients.
 * It allows navigation through the patient list, viewing patient details, filtering, and updating patient records.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class PatientTableUI {
    private static final int PAGE_SIZE = 10;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Scanner scanner;
    private final PatientService patientService;
    private final String doctorId;
    private Patient[] patients;
    private Patient[] filteredPatients;
    private int currentIndex;

    /**
     * Constructs a PatientTableUI instance.
     *
     * @param scanner       the Scanner object used to read user input
     * @param patientService the service used to manage patient-related actions
     * @param doctorId      the ID of the doctor managing the patients
     * @param patients      the initial list of patients
     */
    public PatientTableUI(Scanner scanner, PatientService patientService, String doctorId, Patient[] patients) {
        this.scanner = scanner;
        this.patientService = patientService;
        this.doctorId = doctorId;
        this.patients = patients;
        this.filteredPatients = patients;
        this.currentIndex = 0;
    }

    /**
     * Displays the patient table and provides options for managing patients.
     *
     * @param isUpdating a boolean indicating whether updating patient records is allowed
     */
    public void display(boolean isUpdating) {
        while (true) {
            clearScreen();
            displayTable();

            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
            if (isUpdating== true) System.out.println("U - Update Patient");
            System.out.println("F - Filter Patients");
            System.out.println("C - Clear Filter");
            System.out.println("V - View Patient Details");
            System.out.println("Q - Back to Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "N":
                    if (currentIndex + PAGE_SIZE < filteredPatients.length) {
                        currentIndex += PAGE_SIZE;
                    }
                    break;

                case "P":
                    if (currentIndex - PAGE_SIZE >= 0) {
                        currentIndex -= PAGE_SIZE;
                    }
                    break;
                case "U":
                    if (isUpdating == true) updatePatient();
                    break;
                case "F":
                    filterPatients();
                    break;

                case "C":
                    clearFilter();
                    break;

                case "V":
                    viewPatientDetails();
                    break;

                case "Q":
                    return;

                default:
                    System.out.println("Invalid option. Press Enter to continue...");
                    scanner.nextLine();
            }
        }
    }

    /**
     * Updates a patient's medical record by adding a diagnosis or treatment.
     */
    private void updatePatient() {
        System.out.print("\nEnter patient ID to update: ");
        String patientId = scanner.nextLine().trim();

        Patient patient = Arrays.stream(filteredPatients)
            .filter(p -> p.getId().equals(patientId))
            .findFirst()
            .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        while (true) {
            clearScreen();

            // Display current patient info header
            String headerFormat = "| %-47s |%n";
            String separator = "+-------------------------------------------------+%n";
            System.out.format(separator);
            System.out.format(headerFormat, "UPDATE PATIENT RECORD");
            System.out.format(separator);
            System.out.format("| Patient: %-41s |%n", patient.getName());
            System.out.format("| ID: %-45s |%n", patient.getId());
            System.out.format(separator);

            // Display options
            System.out.println("\nChoose record type to add:");
            System.out.println("1. Add Diagnosis");
            System.out.println("2. Add Treatment");
            System.out.println("Q. Back to Patient List");
            System.out.print("\nEnter choice: ");

            String choice = scanner.nextLine().toUpperCase();

            if (choice.equals("Q")) break;

            MedicalRecordType recordType;
            String typePrompt;

            switch (choice) {
                case "1":
                    recordType = MedicalRecordType.DIAGNOSIS;
                    typePrompt = "diagnosis";
                    break;
                case "2":
                    recordType = MedicalRecordType.TREATMENT;
                    typePrompt = "treatment";
                    break;
                default:
                    System.out.println("Invalid option. Press Enter to continue...");
                    scanner.nextLine();
                    continue;
            }

            // Get record details
            System.out.println("\nEnter " + typePrompt + " details:");
            System.out.println("(Press Enter with empty input to cancel)");
            String details = scanner.nextLine().trim();

            if (details.isEmpty()) {
                continue;
            }

            // Confirm with user
            System.out.print("\nAdd this " + typePrompt + "? (Y/N): ");
            if (!scanner.nextLine().trim().toUpperCase().equals("Y")) {
                continue;
            }

            // Create medical record
            String error = patientService.createMedicalRecord(
                patient.getId(),
                doctorId,
                recordType,
                details
            );

            if (error != null) {
                System.out.println("Error: " + error);
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            } else {
                System.out.println(typePrompt.substring(0, 1).toUpperCase() +
                        typePrompt.substring(1) + " added successfully!");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Views the details of a selected patient, including medical records and vital signs.
     */
    private void viewPatientDetails() {
        System.out.print("\nEnter patient ID to view details: ");
        String patientId = scanner.nextLine().trim();

        Patient patient = Arrays.stream(filteredPatients)
            .filter(p -> p.getId().equals(patientId))
            .findFirst()
            .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        MedicalRecordDetail[] records = patientService.getMedicalRecordsByPatientId(patientId);
        PatientVital vital = patientService.getLatestPatientVitalByPatientId(patientId);

        clearScreen();
        PatientRecordUI.display(patient, scanner, records, vital);
    }

    /**
     * Displays the table of patients with pagination.
     */
    private void displayTable() {
        String format = "| %-10s | %-20s | %-5s | %-8s | %-19s | %-19s | %-19s |%n";
        String separator = "+------------+----------------------+-------+----------+---------------------+---------------------+---------------------+%n";

        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| PATIENT LIST                                                                                                   |%n");
        System.out.format(separator);
        System.out.format(format, "ID", "Name", "Age", "Gender", "Last Appointment", "Created At", "Updated At");
        System.out.format(separator);

        int endIndex = Math.min(currentIndex + PAGE_SIZE, filteredPatients.length);

        for (int i = currentIndex; i < endIndex; i++) {
            Patient patient = filteredPatients[i];
            LocalDateTime lastAppointment = patientService.lastAppointmentWithDoctor(doctorId, patient.getId());
            String lastAppointmentStr = lastAppointment != null ?
                    lastAppointment.format(dateFormat) : "No appointment";

            System.out.format(format,
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    lastAppointmentStr,
                    patient.getCreatedAt().format(dateFormat),
                    patient.getUpdatedAt().format(dateFormat)
            );
        }
        System.out.format(separator);

        displayPagination();
    }

    /**
     * Displays the current page and total pages for the patient list.
     */
    private void displayPagination() {
        int currentPage = (currentIndex / PAGE_SIZE) + 1;
        int totalPages = (int) Math.ceil((double) filteredPatients.length / PAGE_SIZE);
        System.out.format("Page %d of %d (Total records: %d)%n", currentPage, totalPages, filteredPatients.length);
    }

    /**
     * Filters the patient list by age or gender based on user input.
     */
    private void filterPatients() {
        System.out.println("\nFilter by:");
        System.out.println("1. Age");
        System.out.println("2. Gender");
        System.out.print("Choose filter option (or press Enter to cancel): ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                filterByAge();
                break;
            case "2":
                filterByGender();
                break;
            default:
                break;
        }
    }

    /**
     * Filters the patient list by age range.
     */
    private void filterByAge() {
        System.out.print("\nEnter minimum age: ");
        String minAgeStr = scanner.nextLine();
        System.out.print("Enter maximum age: ");
        String maxAgeStr = scanner.nextLine();

        try {
            int minAge = minAgeStr.isEmpty() ? 0 : Integer.parseInt(minAgeStr);
            int maxAge = maxAgeStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxAgeStr);

            filteredPatients = Arrays.stream(patients)
                    .filter(patient -> patient.getAge() >= minAge && patient.getAge() <= maxAge)
                    .toArray(Patient[]::new);
            currentIndex = 0;
        } catch (NumberFormatException e) {
            System.out.println("Invalid age format. Filter cancelled.");
            scanner.nextLine();
        }
    }

    /**
     * Filters the patient list by gender.
     */
    private void filterByGender() {
        System.out.println("\nSelect gender:");
        System.out.println("1. Male");
        System.out.println("2. Female");
        System.out.print("Choose gender: ");

        String choice = scanner.nextLine();
        String targetGender;

        switch (choice) {
            case "1":
                targetGender = "MALE";
                break;
            case "2":
                targetGender = "FEMALE";
                break;
            default:
                return;
        }

        filteredPatients = Arrays.stream(patients)
                .filter(patient -> patient.getGender().toString().equals(targetGender))
                .toArray(Patient[]::new);
        currentIndex = 0;
    }

    /**
     * Clears any applied filters and resets the patient list to the original list.
     */
    private void clearFilter() {
        filteredPatients = patients;
        currentIndex = 0;
    }

    /**
     * Clears the console screen by flushing the terminal output.
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
