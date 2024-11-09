package ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

import enums.AppointmentAction;
import enums.AppointmentStatus;
import enums.UserRole;
import model.AppointmentDetail;
import model.User;
import service.AppointmentScheduleService;
import service.AppointmentService;

public class AppointmentTableUI {
    private static final int COLUMNS = 5; // ID, Doctor Name, Patient Name, Start Date, End Date
    private final AppointmentService appointmentService;
    private final AppointmentScheduleService appointmentScheduleService;
    private final User user;
    private final UserRole role;
    private AppointmentDetail[] appointments;
    
    public AppointmentTableUI(
        AppointmentService appointmentService,
        AppointmentScheduleService appointmentScheduleService,
        User user,
        UserRole role
    ) {
        this.appointmentService = appointmentService;
        this.appointmentScheduleService = appointmentScheduleService;
        this.user = user;
        this.role = role;
        refreshAppointments(); // Load initial data
    }
    
    public void display(Scanner scanner, AppointmentAction action) {
        final int PAGE_SIZE = 10;
        int currentIndex = 0;
        
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
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
                    if(action == AppointmentAction.CANCEL){
                        handleCancel(scanner);
                    }
                    
                case "Q":
                    return;
                    
                default:
                    System.out.println("Invalid option. Press Enter to continue...");
                    scanner.nextLine();
            }
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
            this.appointments = appointmentService.findPendingApprovedByPatient(user.getId());
            return;
        }
        // Add other role conditions here when needed
        this.appointments = appointmentService.findAll();
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