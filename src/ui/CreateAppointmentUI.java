package ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import model.Patient;
import service.AppointmentScheduleService;
import service.AppointmentService;

/**
 * The CreateAppointmentUI class provides a user interface for creating new appointments.
 * It allows the user to input details such as the doctor, date, and time, and ensures
 * that the selected appointment slot is available before finalizing the booking.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class CreateAppointmentUI {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Displays the user interface for creating an appointment.
     * Prompts the user to input doctor details, appointment time, and validates the slot's availability.
     * Once all details are validated, the appointment is created.
     *
     * @param scanner                    the Scanner object used to read user input
     * @param appointmentService         the service used to manage appointments
     * @param appointmentScheduleService the service used to validate appointment slots
     * @param patient                    the patient for whom the appointment is being created
     */
    public static void display(Scanner scanner, AppointmentService appointmentService, AppointmentScheduleService appointmentScheduleService, Patient patient) {
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
        boolean isSlotValid = false;

        while (!isSlotValid) {
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

            // Check if the slot is free
            if (appointmentScheduleService.isSlotFree(doctorId, patient.getId(), startDateTime, endDateTime)) {
                isSlotValid = true;
            } else {
                System.out.format(leftAlignFormat, "Please choose another time slot");
                System.out.format(separator);
                startDateTime = null; // Reset to ask for new time
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
