package ui;

import java.util.Scanner;
import model.Doctor;
import service.DoctorService;
import validator.InputValidator;

/**
 * The DoctorAvailabilityUI class provides a user interface for setting a doctor's availability.
 * It allows the user to input start and end working hours, validates the inputs, and updates the doctor's availability.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0
 */
public class DoctorAvailabilityUI {

    /**
     * Displays the user interface for setting the availability of a doctor.
     * Prompts the user to input start and end working hours, validates the inputs,
     * and updates the doctor's availability through the DoctorService.
     *
     * @param scanner       the Scanner object used to capture user input
     * @param doctorService the service used to manage doctor-related actions
     * @param doctor        the doctor whose availability is being updated
     */
    public static void display(Scanner scanner, DoctorService doctorService, Doctor doctor) {
        // Define formatting
        String leftAlignFormat = "| %-30s |%n";
        String separator = "+--------------------------------+%n";

        // Print the header
        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| SET DOCTOR AVAILABILITY           |%n");
        System.out.format(separator);

        // Display doctor information
        System.out.format("| DOCTOR DETAILS                    |%n");
        System.out.format(separator);
        System.out.format(leftAlignFormat, "Doctor: " + doctor.getName());
        System.out.format(leftAlignFormat, "ID: " + doctor.getId());
        System.out.format(separator);

        // Get start work hours
        String startHoursStr = "";
        int startWorkHours = -1;
        while (startWorkHours == -1) {
            System.out.format(leftAlignFormat, "Enter start hour (0-23):");
            startHoursStr = scanner.nextLine().trim();

            if (InputValidator.validateWorkingHours(startHoursStr)) {
                startWorkHours = Integer.parseInt(startHoursStr);
            } else {
                System.out.format(leftAlignFormat, "Invalid hour! Must be 0-23");
                System.out.format(separator);
            }
        }

        // Get end work hours
        String endHoursStr = "";
        int endWorkHours = -1;
        while (endWorkHours == -1) {
            System.out.format(leftAlignFormat, "Enter end hour (0-23):");
            endHoursStr = scanner.nextLine().trim();

            if (InputValidator.validateWorkingHours(endHoursStr)) {
                endWorkHours = Integer.parseInt(endHoursStr);
                if (endWorkHours <= startWorkHours) {
                    System.out.format(leftAlignFormat, "Must be after start hour!");
                    System.out.format(separator);
                    endWorkHours = -1;  // Reset to continue loop
                }
            } else {
                System.out.format(leftAlignFormat, "Invalid hour! Must be 0-23");
                System.out.format(separator);
            }
        }

        // Display the working hours
        System.out.format(separator);
        System.out.format(leftAlignFormat, "Working hours: " + startWorkHours + ":00 - " + endWorkHours + ":00");
        System.out.format(separator);

        // Update availability
        String result = doctorService.setAvailability(doctor.getId(), startWorkHours, endWorkHours);

        if (result == null) {
            System.out.format(leftAlignFormat, "Availability updated successfully!");
        } else {
            System.out.format(leftAlignFormat, "Error: " + result);
        }
        System.out.format(separator);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
