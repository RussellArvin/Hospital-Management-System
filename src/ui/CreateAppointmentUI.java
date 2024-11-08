package ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import model.Patient;
import service.AppointmentService;

public class CreateAppointmentUI {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public static void display(Scanner scanner, AppointmentService appointmentService, Patient patient) {
        // Define formatting
        String leftAlignFormat = "| %-30s |%n";
        String separator = "+--------------------------------+%n";

        // Print the header
        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| CREATE NEW APPOINTMENT             |%n");
        System.out.format(separator);

        // Display patient information
        System.out.format("| PATIENT DETAILS                    |%n");
        System.out.format(separator);
        System.out.format(leftAlignFormat, "Patient: " + patient.getName());
        System.out.format(leftAlignFormat, "ID: " + patient.getId());
        System.out.format(separator);

        // Get doctor name and search for ID
        String doctorId = null;
        while (doctorId == null) {
            System.out.format(leftAlignFormat, "Enter Doctor Name:");
            String doctorName = scanner.nextLine().trim();
            
            doctorId = appointmentService.getDoctorId(doctorName);
            if (doctorId == null) {
                System.out.format(leftAlignFormat, "Doctor not found! Try again.");
                System.out.format(separator);
                continue;
            }
            System.out.format(leftAlignFormat, "Doctor ID: " + doctorId);
        }

        // Get appointment start date and time
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        while (startDateTime == null) {
            System.out.format(leftAlignFormat, "Enter Start Date/Time:");
            System.out.format(leftAlignFormat, "(Format: YYYY-MM-DD HH:mm)");
            String startDateTimeStr = scanner.nextLine().trim();
            
            try {
                startDateTime = LocalDateTime.parse(startDateTimeStr, formatter);
                endDateTime = startDateTime.plusMinutes(30); // Set end time to 30 minutes after start time
                
                // Display the appointment time details
                System.out.format(separator);
                System.out.format(leftAlignFormat, "Start Time: " + startDateTime.format(formatter));
                System.out.format(leftAlignFormat, "End Time: " + endDateTime.format(formatter));
                System.out.format(leftAlignFormat, "Duration: 30 minutes");
                System.out.format(separator);
                
            } catch (DateTimeParseException e) {
                System.out.format(leftAlignFormat, "Invalid date format!");
                continue;
            }
        }

        // Create the appointment
        String result = appointmentService.createAppointment(patient.getId(), doctorId, startDateTime, endDateTime);
        
        System.out.format(separator);
        if (result == null) {
            System.out.format(leftAlignFormat, "Appointment created successfully!");
        } else {
            System.out.format(leftAlignFormat, "Error: " + result);
        }
        System.out.format(separator);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}