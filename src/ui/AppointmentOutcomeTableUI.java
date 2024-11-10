package ui;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

import model.AppointmentOutcomeDetail;
import model.Patient;
import model.Prescription;
import model.PrescriptionWithMedicine;
import service.AppointmentOutcomeService;
import enums.PrescriptionStatus;
import enums.UserRole;

public class AppointmentOutcomeTableUI {
    private static final int COLUMNS = 6;
    private final AppointmentOutcomeService appointmentOutcomeService;
    private AppointmentOutcomeDetail[] outcomes;
    private Patient patient;
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public AppointmentOutcomeTableUI(AppointmentOutcomeService appointmentOutcomeService, Patient patient) {
        this.appointmentOutcomeService = appointmentOutcomeService;
        this.patient = patient;
    }
    
    private void refreshOutcomes() {
        if(patient == null) this.outcomes = appointmentOutcomeService.findAllConfirmed();
        else this.outcomes = appointmentOutcomeService.findAllPatientCompleted(patient.getId());
    }
    
    public void display(Scanner scanner, boolean isDispensing) {
        refreshOutcomes();
        
        final int PAGE_SIZE = 5;
        int currentIndex = 0;
        
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            displayTable(currentIndex, PAGE_SIZE);
            
            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
            if(isDispensing) System.out.println("D - Dispense Medicine");
            System.out.println("V - View Outcome Details");
            System.out.println("Q - Back to Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().toUpperCase();
            
            switch (choice) {
                case "N":
                    if (currentIndex + PAGE_SIZE < outcomes.length) {
                        currentIndex += PAGE_SIZE;
                    }
                    break;
                    
                case "P":
                    if (currentIndex - PAGE_SIZE >= 0) {
                        currentIndex -= PAGE_SIZE;
                    }
                    break;
                    
                case "D":
                    if(isDispensing) handleDispensing(scanner);
                    break;
                    
                case "V":
                    handleViewDetails(scanner);
                    break;
                    
                case "Q":
                    return;
                    
                default:
                    System.out.println("Invalid option. Press Enter to continue...");
                    scanner.nextLine();
            }
        }
    }

    private void handleViewDetails(Scanner scanner) {
        System.out.print("Enter Outcome ID to view details: ");
        String outcomeId = scanner.nextLine();
        
        AppointmentOutcomeDetail outcome = findOutcomeById(outcomeId);
        if (outcome == null) {
            System.out.println("Error: Outcome not found");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nOutcome Details:");
        System.out.println("----------------------------------------");
        System.out.println("Outcome ID: " + outcome.getId());
        System.out.println("Appointment Date: " + 
            outcome.getAppointment().getStartDateTime().format(dateFormat));
        System.out.println("Doctor: " + outcome.getAppointment().getDoctor().getName());
        System.out.println("Patient: " + outcome.getAppointment().getPatient().getName());
        System.out.println("Service Type: " + outcome.getServiceType());
        System.out.println("\nConsultation Notes:");
        System.out.println(outcome.getConsultationNotes());
        System.out.println("\nPrescriptions:");
        
        for (PrescriptionWithMedicine prescription : outcome.getPrescriptions()) {
            System.out.printf("- %s (Amount: %d) - %s%n",
                prescription.getMedicine().getName(),
                prescription.getAmount(),
                prescription.getStatus());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void handleDispensing(Scanner scanner) {
        try {
            System.out.print("Enter Outcome ID: ");
            String outcomeId = scanner.nextLine();
            
            AppointmentOutcomeDetail outcome = findOutcomeById(outcomeId);
            if (outcome == null) {
                System.out.println("Error: Outcome not found");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }

            System.out.println("\nPrescriptions for this outcome:");
            for (PrescriptionWithMedicine prescription : outcome.getPrescriptions()) {
                System.out.printf("- %s (Amount: %d) - %s%n",
                    prescription.getMedicine().getName(),
                    prescription.getAmount(),
                    prescription.getStatus());
            }
            
            System.out.print("\nEnter Medicine Name to dispense: ");
            String medicineName = scanner.nextLine();
            
            Prescription prescription = findPrescriptionByMedicine(outcome, medicineName);
            if (prescription == null) {
                System.out.println("Error: Prescription not found for this medicine");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }
            
            if (prescription.getStatus() == PrescriptionStatus.DISPENSED) {
                System.out.println("Error: Medicine already dispensed");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }
            
            String error = appointmentOutcomeService.dispenseMedicine(prescription.getId());
            if (error != null) {
                System.out.println("Error: " + error);
            } else {
                System.out.println("Medicine dispensed successfully!");
                refreshOutcomes();
            }
            
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private AppointmentOutcomeDetail findOutcomeById(String outcomeId) {
        return Arrays.stream(outcomes)
            .filter(outcome -> outcome.getId().equals(outcomeId))
            .findFirst()
            .orElse(null);
    }
    
    private Prescription findPrescriptionByMedicine(AppointmentOutcomeDetail outcome, String medicineName) {
        return Arrays.stream(outcome.getPrescriptions())
            .filter(prescription -> prescription.getMedicine().getName().equalsIgnoreCase(medicineName))
            .findFirst()
            .orElse(null);
    }
    
    private void displayTable(int startIndex, int pageSize) {
        String mainSeparator = "+--------------------------------------+----------------------+----------------------+%n";
        String prescriptionSeparator = "+                                      +                      +                      +----------------------+----------+------------+%n";
        String headerFormat = "| %-36s | %-20s | %-20s |%n";
        String prescriptionFormat = "|                                      |                      |                      | %-20s | %-8d | %-10s |%n";
        
        System.out.format("%n");
        System.out.format(mainSeparator);
        System.out.format("| %-82s |%n", "APPOINTMENT OUTCOMES");
        System.out.format(mainSeparator);
        System.out.format("| %-36s | %-20s | %-20s | %-20s | %-8s | %-10s |%n", 
            "Outcome ID", "Doctor", "Patient", "Medicine", "Amount", "Status");
        System.out.format(mainSeparator.replace("+%n", "+----------------------+----------+------------+%n"));

        if (outcomes.length == 0) {
            System.out.format("| %-124s |%n", "No pending prescriptions found");
            System.out.format(mainSeparator.replace("+%n", "+----------------------+----------+------------+%n"));
            return;
        }
        
        int displayedOutcomes = 0;
        
        // Iterate through outcomes based on pagination
        for (int i = startIndex; i < outcomes.length && displayedOutcomes < pageSize; i++) {
            AppointmentOutcomeDetail outcome = outcomes[i];
            
            // Print outcome header
            System.out.format(headerFormat,
                outcome.getId(),
                outcome.getAppointment().getDoctor().getName(),
                outcome.getAppointment().getPatient().getName()
            );
            
            // Print each prescription for this outcome
            PrescriptionWithMedicine[] prescriptions = outcome.getPrescriptions();
            if (prescriptions.length == 0) {
                System.out.format(prescriptionFormat, "No prescriptions", 0, "N/A");
            } else {
                for (PrescriptionWithMedicine prescription : prescriptions) {
                    System.out.format(prescriptionFormat,
                        prescription.getMedicine().getName(),
                        prescription.getAmount(),
                        prescription.getStatus()
                    );
                }
            }
            
            // Print separator between outcomes
            if (i < outcomes.length - 1 && displayedOutcomes < pageSize - 1) {
                System.out.format(mainSeparator);
            }
            
            displayedOutcomes++;
        }
        
        System.out.format(mainSeparator.replace("+%n", "+----------------------+----------+------------+%n"));
        
        int currentPage = (startIndex / pageSize) + 1;
        int totalPages = (int) Math.ceil((double) outcomes.length / pageSize);
        System.out.format("Page %d of %d (Total outcomes: %d)%n", 
            currentPage, totalPages, outcomes.length);
        
        // Add a summary section
        if(patient == null){
            System.out.println("\nSummary of Pending Prescriptions:");
            for (AppointmentOutcomeDetail outcome : outcomes) {
                long pendingCount = Arrays.stream(outcome.getPrescriptions())
                    .filter(p -> p.getStatus() == PrescriptionStatus.PENDING)
                    .count();
                if (pendingCount > 0) {
                    System.out.format("Outcome %s: %d pending prescription(s)%n",
                        outcome.getId(), pendingCount);
                }
            }
        }
    }
}