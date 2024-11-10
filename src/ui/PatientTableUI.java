package ui;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import model.Patient;
import service.PatientService;
import enums.Gender;

public class PatientTableUI {
    private static final int COLUMNS = 7;
    private static final int PAGE_SIZE = 10;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Scanner scanner;
    private final PatientService patientService;
    private final String doctorId;
    private Patient[] patients;
    private Patient[] filteredPatients;
    private int currentIndex;

    public PatientTableUI(Scanner scanner, PatientService patientService, String doctorId, Patient[] patients) {
        this.scanner = scanner;
        this.patientService = patientService;
        this.doctorId = doctorId;
        this.patients = patients;
        this.filteredPatients = patients;
        this.currentIndex = 0;
    }

    public void display() {
        while (true) {
            clearScreen();
            displayTable();
            
            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
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
        
        clearScreen();
        new PatientRecordUI().display(patient, scanner);
    }

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

    private void displayPagination() {
        int currentPage = (currentIndex / PAGE_SIZE) + 1;
        int totalPages = (int) Math.ceil((double) filteredPatients.length / PAGE_SIZE);
        System.out.format("Page %d of %d (Total records: %d)%n", currentPage, totalPages, filteredPatients.length);
    }

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

    private void clearFilter() {
        filteredPatients = patients;
        currentIndex = 0;
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}