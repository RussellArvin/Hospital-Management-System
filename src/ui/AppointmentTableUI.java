package ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import enums.AppointmentAction;
import enums.AppointmentServiceType;
import enums.AppointmentStatus;
import enums.UserRole;
import model.AppointmentDetail;
import model.PendingPrescription;
import model.User;
import service.AppointmentOutcomeService;
import service.AppointmentScheduleService;
import service.AppointmentService;
import service.InventoryService;

public class AppointmentTableUI {
    private final AppointmentService appointmentService;
    private final AppointmentScheduleService appointmentScheduleService;
    private AppointmentOutcomeService appointmentOutcomeService;
    private InventoryService inventoryService;
    private final User user;
    private final UserRole role;
    private AppointmentAction action;
    private AppointmentDetail[] appointments;
    
    public AppointmentTableUI(
        AppointmentService appointmentService,
        AppointmentScheduleService appointmentScheduleService,
        AppointmentOutcomeService appointmentOutcomeService,
        InventoryService inventoryService,
        User user,
        UserRole role
    ) {
        this.appointmentService = appointmentService;
        this.appointmentScheduleService = appointmentScheduleService;
        this.appointmentOutcomeService = appointmentOutcomeService;
        this.inventoryService = inventoryService;
        this.user = user;
        this.role = role;
        this.action = null;
        //refreshAppointments(); // Load initial data
    }
    
    public void display(Scanner scanner, AppointmentAction action) {
        final int PAGE_SIZE = 10;
        int currentIndex = 0;

        this.action = action;
        refreshAppointments();
        
        while (true) {
            //System.out.print("\033[H\033[2J");
            //System.out.flush();
            
            displayTable(currentIndex, PAGE_SIZE);
            
            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
            
            // Display action-specific options
            switch (action) {
                case RESCHEDULE:
                    System.out.println("R - Reschedule Appointment");
                    break;
                case APPROVE:
                    System.out.println("A - Approve Appointment");
                    System.out.println("D - Reject Appointment");
                    break;
                case CANCEL:
                    System.out.println("C - Cancel Appointment");
                    break;
                case OUTCOME:
                    System.out.println("O - Record Outcome");
                    break;
                default:
                    break;
            }
            
            System.out.println("Q - Back to Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().toUpperCase();
            
            switch (choice) {
                case "N":
                    if (currentIndex + PAGE_SIZE < appointments.length) {
                        currentIndex += PAGE_SIZE;
                    }
                    break;
                    
                case "P":
                    if (currentIndex - PAGE_SIZE >= 0) {
                        currentIndex -= PAGE_SIZE;
                    }
                    break;
                    
                case "R":
                    if (action == AppointmentAction.RESCHEDULE) {
                        handleReschedule(scanner);
                    }
                    break;
                case "A":
                    if (action == AppointmentAction.APPROVE) {
                        handleApprove(scanner);
                    }
                    break;
                    
                case "D":
                    if (action == AppointmentAction.APPROVE) {
                        handleReject(scanner);
                    }
                    break;
                case "C":
                    if (action == AppointmentAction.CANCEL) {
                        handleCancel(scanner);
                    }
                    break;
                case "O":
                    if (action == AppointmentAction.OUTCOME) {
                        handleOutcome(scanner);
                    }
                    break;
                    
                case "Q":
                    return;
                    
                default:
                    System.out.println("Invalid option. Press Enter to continue...");
                    scanner.nextLine();
            }
        }
    }

    private AppointmentDetail findAppointmentById(String appointmentId) {
        return Arrays.stream(appointments)
            .filter(app -> app.getId().equals(appointmentId))
            .findFirst()
            .orElse(null);
    }

    private void handleOutcome(Scanner scanner) {
        try {
            System.out.print("Enter appointment ID: ");
            String appointmentId = scanner.nextLine();
            
            AppointmentDetail appointment = findAppointmentById(appointmentId);
            if (appointment == null) {
                System.out.println("Error: Appointment not found");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }
            
            System.out.println("\nSelect Service Type:");
            System.out.println("1. CONSULTATION");
            System.out.println("2. XRAY");
            System.out.println("3. BLOOD_TEST");
            System.out.println("4. CHECKUP");
            System.out.println("5. SURGERY");
            System.out.print("Enter choice (1-5): ");
            
            int serviceChoice = Integer.parseInt(scanner.nextLine());
            AppointmentServiceType serviceType;
            switch (serviceChoice) {
                case 1:
                    serviceType = AppointmentServiceType.CONSULTATION;
                    break;
                case 2:
                    serviceType = AppointmentServiceType.XRAY;
                    break;
                case 3:
                    serviceType = AppointmentServiceType.BLOOD_TEST;
                    break;
                case 4:
                    serviceType = AppointmentServiceType.CHECKUP;
                    break;
                case 5:
                    serviceType = AppointmentServiceType.SURGERY;
                default:
                    System.out.println("Invalid service type");
                    return;
            }
            
            System.out.println("\nEnter consultation notes:");
            String consultationNotes = scanner.nextLine();
            
            List<PendingPrescription> prescriptions = new ArrayList<>();
            while (true) {
                System.out.print("\nEnter medicine name (or press Enter to finish): ");
                String medicineName = scanner.nextLine();
                
                if (medicineName.isEmpty()) {
                    break;
                }
                
                // Assuming there's a method to get medicine ID from name
                String medicineId = inventoryService.getMedicineId(medicineName);
                if (medicineId == null) {
                    System.out.println("Medicine not found. Please try again.");
                    continue;
                }
                
                System.out.print("Enter amount: ");
                int amount = Integer.parseInt(scanner.nextLine());
                
                prescriptions.add(new PendingPrescription(medicineId, amount));
            }
            
            PendingPrescription[] prescriptionsArray = prescriptions.toArray(new PendingPrescription[0]);
            
            String error = appointmentOutcomeService.createOutcome(
                appointmentId,
                serviceType,
                consultationNotes,
                prescriptionsArray
            );
            
            if (error != null) {
                System.out.println("Error: " + error);
            } else {
                System.out.println("Outcome recorded successfully!");
            }
            
            System.out.println("Press Enter to continue...");
            scanner.nextLine();

            refreshAppointments();
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private void handleReschedule(Scanner scanner) {
        try {
            System.out.print("Enter appointment ID to reschedule: ");
            String appointmentId = scanner.nextLine();
            
            System.out.println("Enter new date and time (yyyy-MM-dd HH:mm): ");
            String dateTimeStr = scanner.nextLine();
            
            LocalDateTime startDateTime = LocalDateTime.parse(dateTimeStr, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            LocalDateTime endDateTime = startDateTime.plusMinutes(30);

            AppointmentDetail appointment = Arrays.stream(appointments)
            .filter(app -> app.getId().equals(appointmentId))
            .findFirst()
            .orElse(null);



            if(appointmentScheduleService.isSlotFree(appointment.getDoctorId(),appointment.getPatientId(),startDateTime,endDateTime)){
                System.out.println("Please choose another time slot");
                return;
            }
            
            String error = appointmentService.rescheduleAppointment(appointmentId, startDateTime, endDateTime);
            if (error != null) {
                System.out.println("Error: " + error);
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            } else {
                refreshAppointments();
            }
        } catch (Exception e) {
            System.out.println("Error: Invalid date/time format");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }
    
    
    private void handleApprove(Scanner scanner) {
        System.out.print("Enter appointment ID to approve: ");
        String appointmentId = scanner.nextLine();
        
        String error = appointmentService.setStatus(appointmentId, AppointmentStatus.CONFIRMED,null);
        if (error != null) {
            System.out.println("Error: " + error);
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        } else {
            refreshAppointments();
        }
    }

    private void handleCancel(Scanner scanner) {
        System.out.print("Enter appointment ID to approve: ");
        String appointmentId = scanner.nextLine();
        
        String error = appointmentService.setStatus(appointmentId, AppointmentStatus.PATIENT_CANCELLED,null);
        if (error != null) {
            System.out.println("Error: " + error);
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        } else {
            refreshAppointments();
        }
    }
    
    private void handleReject(Scanner scanner) {
        System.out.print("Enter appointment ID to deny: ");
        String appointmentId = scanner.nextLine();
        
        System.out.print("Enter reason for denial: ");
        String reason = scanner.nextLine();
        
        String error = appointmentService.setStatus(appointmentId, AppointmentStatus.DOCTOR_CANCELLED, reason);
        if (error != null) {
            System.out.println("Error: " + error);
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        } else {
            refreshAppointments();
        }
    }

    private void refreshAppointments() {
        if(role == UserRole.PATIENT){
            switch (action) {
                case CANCEL:
                case RESCHEDULE:
                    this.appointments = appointmentService.findPendingApprovedByPatient(user.getId());
                    return;
                case VIEW:
                    this.appointments = appointmentService.findApprovedByPatient(user.getId());
                    return;
                default:
                    break;
            }
        }
        else if(role == UserRole.DOCTOR){
            switch (action) {
                case OUTCOME:
                case VIEW:
                    this.appointments = appointmentService.findApprovedByDoctor(user.getId());
                    return;
                case APPROVE:
                    this.appointments = appointmentService.findRequestedByDoctor(user.getId());
                    return;
                default:
                    break;
            }
        }
        else if(role == UserRole.ADMINISTRATOR){
            this.appointments = this.appointmentService.findCompleted();
            return;
        }
        // Add other role conditions here when needed
        this.appointments = null;
    }
    
    private void displayTable(int startIndex, int pageSize) {
        String format = "| %-36s | %-20s | %-20s | %-19s | %-19s |%n";
        String separator = "+--------------------------------------+----------------------+----------------------+---------------------+---------------------+%n";
        
        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| %-118s |%n", "APPOINTMENT LIST");
        System.out.format(separator);
        System.out.format(format, "ID", "Doctor Name", "Patient Name", "Start Date", "End Date");
        System.out.format(separator);
        
        int endIndex = Math.min(startIndex + pageSize, appointments.length);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (int i = startIndex; i < endIndex; i++) {
            AppointmentDetail appointment = appointments[i];
            
            System.out.format(format,
                appointment.getId(),
                appointment.getDoctor().getName(),
                appointment.getPatient().getName(),
                appointment.getStartDateTime().format(dateFormat),
                appointment.getEndDateTime().format(dateFormat)
            );
        }
        System.out.format(separator);
        
        int currentPage = (startIndex / pageSize) + 1;
        int totalPages = (int) Math.ceil((double) appointments.length / pageSize);
        System.out.format("Page %d of %d (Total records: %d)%n", currentPage, totalPages, appointments.length);
    }
}