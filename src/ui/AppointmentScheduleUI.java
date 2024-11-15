package ui;

import enums.UserRole;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.AppointmentDetail;
import model.Doctor;
import service.AppointmentService;
import util.Constant;

/**
 * The AppointmentScheduleUI class provides a user interface for viewing and interacting
 * with the appointment schedule for a specific doctor or patient. It supports displaying
 * the schedule, navigating between days, and handling various user options.
 * 
 * @author Russell Arvin 
 * @version 1.0
 */
public class AppointmentScheduleUI {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final int HOURS_PER_COLUMN = 6;
    private final int TOTAL_COLUMNS = 4;
    
    private final Scanner scanner;
    private final AppointmentService appointmentService;
    private final UserRole userRole;
    private final Doctor doctor;
    
    private String doctorId;
    private String doctorName;
    private int startWorkHours;
    private int endWorkHours;
    private LocalDate currentDate;
    private final LocalDate today;

    /**
     * Constructor for the AppointmentScheduleUI.
     *
     * @param scanner the Scanner object for reading user input
     * @param appointmentService the service responsible for handling appointments
     * @param userRole the role of the current user (e.g., patient or staff)
     * @param doctor the doctor associated with the schedule (optional, can be null)
     */
    public AppointmentScheduleUI(Scanner scanner, AppointmentService appointmentService, 
                               UserRole userRole, Doctor doctor) {
        this.scanner = scanner;
        this.appointmentService = appointmentService;
        this.userRole = userRole;
        this.doctor = doctor;
        this.today = LocalDate.now();
        this.currentDate = today;
    }

    /**
     * Initializes doctor information based on the user's role. Prompts for a doctor's
     * information if the user is a patient or validates and sets the doctor's information
     * for staff users.
     */
    private void initializeDoctorInfo() {
        if (userRole == UserRole.PATIENT) {
            promptForDoctorInfo();
        } else {
            validateAndSetDoctorInfo();
        }
    }

    /**
     * Prompts the user to enter a doctor's name and retrieves the doctor's ID.
     * If the doctor is not found, the user is prompted to try again.
     */
    private void promptForDoctorInfo() {
        while (doctorId == null) {
            System.out.print("Enter Doctor Name: ");
            doctorName = scanner.nextLine().trim();
            
            doctorId = appointmentService.getDoctorId(doctorName);
            if (doctorId == null) {
                System.out.println("Doctor not found! Please try again.");
            }
        }
        startWorkHours = Constant.DEFAULT_START_WORK_HOURS;
        endWorkHours = Constant.DEFAULT_END_WORK_HOURS;
    }

    /**
     * Validates and sets the doctor's information for staff users.
     * Throws an exception if the doctor object is null.
     */
    private void validateAndSetDoctorInfo() {
        if (doctor == null) {
            throw new IllegalArgumentException("Error: Doctor information not provided");
        }
        doctorId = doctor.getId();
        doctorName = doctor.getName();
        startWorkHours = doctor.getStartWorkHours();
        endWorkHours = doctor.getEndWorkHours();
    }

    /**
     * Displays the appointment schedule UI and handles user navigation and interaction.
     */
    public void display() {
        initializeDoctorInfo();
        while (true) {
            clearScreen();
            AppointmentDetail[] appointments = appointmentService.findDoctorAppointmentsByDate(doctorId, currentDate);
            displaySchedule(appointments);
            
            displayOptions();
            String choice = scanner.nextLine().toUpperCase();
            
            if (!processChoice(choice)) {
                break;
            }
        }
    }

    /**
     * Clears the console screen for a clean UI experience.
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Displays navigation options for the user.
     */
    private void displayOptions() {
        System.out.println("\nOptions:");
        System.out.println("N - Next Day");
        
        if (userRole != UserRole.PATIENT || currentDate.isAfter(today)) {
            System.out.println("P - Previous Day");
        }
        
        if (!currentDate.equals(today)) {
            System.out.println("T - Today");
        }
        System.out.println("J - Jump to Date");
        System.out.println("Q - Back to Menu");
        System.out.print("Choose an option: ");
    }

    /**
     * Processes the user's choice for navigating the schedule or exiting the menu.
     *
     * @param choice the user's choice
     * @return true to continue, false to exit the menu
     */
    private boolean processChoice(String choice) {
        switch (choice) {
            case "N":
                currentDate = currentDate.plusDays(1);
                return true;
            case "P":
                if (userRole != UserRole.PATIENT && !currentDate.isAfter(today)) {
                    currentDate = currentDate.minusDays(1);
                }
                return true;
            case "T":
                if (!currentDate.equals(today)) {
                    currentDate = today;
                }
                return true;
            case "J":
                LocalDate jumpDate = getJumpDate();
                if (jumpDate != null) {
                    currentDate = jumpDate;
                }
                return true;
            case "Q":
                return false;
            default:
                System.out.println("Invalid option. Press Enter to continue...");
                scanner.nextLine();
                return true;
        }
    }

    /**
     * Prompts the user to enter a date for jumping to a specific day in the schedule.
     *
     * @return the selected LocalDate, or null if the input is invalid
     */
    private LocalDate getJumpDate() {
        while (true) {
            System.out.println("\nEnter date (YYYY-MM-DD):");
            String dateStr = scanner.nextLine().trim();
            
            try {
                LocalDate jumpDate = LocalDate.parse(dateStr, inputDateFormatter);
                LocalDate minimumDate = userRole == UserRole.PATIENT ? today : null;
                
                if (minimumDate != null && jumpDate.isBefore(minimumDate)) {
                    System.out.println("Cannot view dates before " + minimumDate.format(inputDateFormatter));
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    return null;
                }
                
                return jumpDate;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return null;
            }
        }
    }
    
    /**
     * Displays the schedule for the specified appointments on the current date.
     *
     * @param appointments the array of appointments to display
     */
    private void displaySchedule(AppointmentDetail[] appointments) {
        String separator = "+-------+------------------------+".repeat(TOTAL_COLUMNS) + "%n";
        
        displayHeader(separator);
        displayColumnHeaders(separator);
        displayTimeSlots(appointments);
        
        System.out.format(separator);
    }

    /**
     * Displays the header section of the schedule.
     *
     * @param separator the separator string for formatting
     */
    private void displayHeader(String separator) {
        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| DR. %-101s |%n", doctorName.toUpperCase());
        System.out.format("| SCHEDULE FOR %-96s |%n", currentDate.format(dateFormatter));
        if (userRole != UserRole.PATIENT) {
            System.out.format("| WORKING HOURS: %02d:00 - %02d:00 %-84s |%n", startWorkHours, endWorkHours, "");
        }
        System.out.format(separator);
    }

    /**
     * Displays the column headers for the schedule.
     *
     * @param separator the separator string for formatting
     */
    private void displayColumnHeaders(String separator) {
        String headerFormat = "| TIME  | %-22s |".repeat(TOTAL_COLUMNS) + "%n";
        System.out.format(headerFormat, 
            "00:00 - 05:30",
            "06:00 - 11:30",
            "12:00 - 17:30",
            "18:00 - 23:30"
        );
        System.out.format(separator);
    }

    /**
     * Displays the time slots in the schedule and their corresponding appointment details.
     *
     * @param appointments the array of appointments to match with time slots
     */
    private void displayTimeSlots(AppointmentDetail[] appointments) {
        for (int halfHour = 0; halfHour < 12; halfHour++) {
            StringBuilder line = new StringBuilder();
            
            for (int column = 0; column < TOTAL_COLUMNS; column++) {
                int hour = (column * HOURS_PER_COLUMN) + (halfHour / 2);
                int minute = (halfHour % 2) * 30;
                
                LocalDateTime timeSlot = currentDate.atTime(hour, minute);
                String time = timeSlot.format(timeFormatter);
                String slotInfo = getSlotInfo(timeSlot, appointments);
                
                line.append(String.format("| %-5s | %-22s |", time, slotInfo));
            }
            System.out.println(line.toString());
        }
    }

    /**
     * Retrieves information about a time slot based on appointments and working hours.
     *
     * @param timeSlot the time slot to check
     * @param appointments the array of appointments to match
     * @return a string describing the status of the time slot
     */
    private String getSlotInfo(LocalDateTime timeSlot, AppointmentDetail[] appointments) {
        int currentHour = timeSlot.getHour();
        boolean isWorkHours = currentHour >= startWorkHours && currentHour < endWorkHours;
        
        AppointmentDetail slotAppointment = findAppointmentForTimeSlot(timeSlot, appointments);
        
        if (slotAppointment != null) {
            return formatAppointmentInfo(slotAppointment);
        }
        
        return isWorkHours ? "Available" : "Not Working";
    }

    /**
     * Finds the appointment that matches a specific time slot.
     *
     * @param timeSlot the time slot to match
     * @param appointments the array of appointments to search
     * @return the matching appointment, or null if no match is found
     */
    private AppointmentDetail findAppointmentForTimeSlot(LocalDateTime timeSlot, AppointmentDetail[] appointments) {
        if (appointments != null) {
            for (AppointmentDetail appointment : appointments) {
                if (timeSlot.isEqual(appointment.getStartDateTime())) {
                    return appointment;
                }
            }
        }
        return null;
    }

    /**
     * Formats the information of an appointment for display.
     *
     * @param appointment the appointment to format
     * @return a string containing the formatted appointment information
     */
    private String formatAppointmentInfo(AppointmentDetail appointment) {
        if (userRole == UserRole.PATIENT) {
            return "Not Available";
        }
        return String.format("%s - %s", 
            appointment.getStatus(),
            appointment.getPatient().getName());
    }
}
