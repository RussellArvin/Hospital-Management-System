package ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.AppointmentDetail;
import model.Doctor;
import enums.UserRole;
import service.AppointmentService;

public class AppointmentScheduleUI {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int HOURS_PER_COLUMN = 6;
    private static final int TOTAL_COLUMNS = 4;
    
    public static void display(Scanner scanner, AppointmentService appointmentService, UserRole role, Doctor doctor) {
        String doctorId = null;
        String doctorName = "";
        int startWorkHours = 9; // default value
        int endWorkHours = 17;  // default value
        
        if (role == UserRole.PATIENT) {
            // For patients, prompt for doctor name
            while (doctorId == null) {
                System.out.print("Enter Doctor Name: ");
                doctorName = scanner.nextLine().trim();
                
                doctorId = appointmentService.getDoctorId(doctorName);
                if (doctorId == null) {
                    System.out.println("Doctor not found! Please try again.");
                    continue;
                }
            }
        } else {
            // For other roles, use the provided doctor object
            if (doctor == null) {
                System.out.println("Error: Doctor information not provided");
                return;
            }
            doctorId = doctor.getId();
            doctorName = doctor.getName();
            startWorkHours = doctor.getStartWorkHours();
            endWorkHours = doctor.getEndWorkHours();
        }
        
        LocalDate currentDate = LocalDate.now();
        LocalDate today = LocalDate.now();
        
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            // Get appointments for the current date
            AppointmentDetail[] appointments = appointmentService.getDoctorAppointmentsByDate(doctorId, currentDate);
            
            displaySchedule(currentDate, appointments, doctorName, startWorkHours, endWorkHours, role);

            System.out.println("\nOptions:");
            System.out.println("N - Next Day");
            
            if (role != UserRole.PATIENT || currentDate.isAfter(today)) {
                System.out.println("P - Previous Day");
            }
            
            if (!currentDate.equals(today)) {
                System.out.println("T - Today");
            }
            System.out.println("J - Jump to Date");
            System.out.println("Q - Back to Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().toUpperCase();
            
            switch (choice) {
                case "N":
                    currentDate = currentDate.plusDays(1);
                    break;
                case "P":
                    if (role != UserRole.PATIENT && !currentDate.isAfter(today)) {
                        currentDate = currentDate.minusDays(1);
                    }
                    break;
                case "T":
                    if (!currentDate.equals(today)) {
                        currentDate = today;
                    }
                    break;
                case "J":
                    LocalDate jumpDate = getJumpDate(scanner, role == UserRole.PATIENT ? today : null);
                    if (jumpDate != null) {
                        currentDate = jumpDate;
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

    private static LocalDate getJumpDate(Scanner scanner, LocalDate minimumDate) {
        while (true) {
            System.out.println("\nEnter date (YYYY-MM-DD):");
            String dateStr = scanner.nextLine().trim();
            
            try {
                LocalDate jumpDate = LocalDate.parse(dateStr, inputDateFormatter);
                
                // For patients, don't allow jumping to past dates
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
    
    private static void displaySchedule(LocalDate date, AppointmentDetail[] appointments, String doctorName, 
        int startWorkHours, int endWorkHours, UserRole role) {
        String columnFormat = "| %-5s | %-22s |";
        String separator = "+-------+------------------------+".repeat(TOTAL_COLUMNS) + "%n";
        
        // Header
        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| DR. %-101s |%n", doctorName.toUpperCase());
        System.out.format("| SCHEDULE FOR %-96s |%n", date.format(dateFormatter));
        if (role != UserRole.PATIENT) {
            System.out.format("| WORKING HOURS: %02d:00 - %02d:00 %-84s |%n", startWorkHours, endWorkHours, "");
        }
        System.out.format(separator);
        
        // Column headers
        String headerFormat = "| TIME  | %-22s |".repeat(TOTAL_COLUMNS) + "%n";
        System.out.format(headerFormat, 
            "00:00 - 05:30",
            "06:00 - 11:30",
            "12:00 - 17:30",
            "18:00 - 23:30"
        );
        System.out.format(separator);
        
        // Generate time slots
        for (int halfHour = 0; halfHour < 12; halfHour++) {
            StringBuilder line = new StringBuilder();
            
            for (int column = 0; column < TOTAL_COLUMNS; column++) {
                int hour = (column * HOURS_PER_COLUMN) + (halfHour / 2);
                int minute = (halfHour % 2) * 30;
                
                LocalDateTime timeSlot = date.atTime(hour, minute);
                String time = timeSlot.format(timeFormatter);
                String slotInfo = getSlotInfo(timeSlot, appointments, startWorkHours, endWorkHours, role);
                
                line.append(String.format("| %-5s | %-22s |", time, slotInfo));
            }
            System.out.println(line.toString());
        }
        
        System.out.format(separator);
    }



    private static String getSlotInfo(LocalDateTime timeSlot, AppointmentDetail[] appointments, 
            int startWorkHours, int endWorkHours, UserRole role) {
        int currentHour = timeSlot.getHour();
        boolean isWorkHours = currentHour >= startWorkHours && currentHour < endWorkHours;
        
        AppointmentDetail slotAppointment = null;
        if (appointments != null) {
            for (AppointmentDetail appointment : appointments) {
                if (timeSlot.isEqual(appointment.getStartDateTime())) {
                    slotAppointment = appointment;
                    break;
                }
            }
        }
        
        if (slotAppointment != null) {
            if (role == UserRole.PATIENT) {
                return "Not Available";
            } else {
                return String.format("%s - %s", 
                    slotAppointment.getStatus(),
                    slotAppointment.getPatient().getName());
            }
        }
        
        if (!isWorkHours) {
            return "Not Working";
        } else {
            return "Available";
        }
    }
}