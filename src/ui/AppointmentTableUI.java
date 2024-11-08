package ui;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.AppointmentDetail;

public class AppointmentTableUI {
    private static final int COLUMNS = 5; // ID, Doctor Name, Patient Name, Start Date, End Date
    
    public static void display(AppointmentDetail[] appointments, Scanner scanner) {
        final int PAGE_SIZE = 10;
        int currentIndex = 0;
        
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            displayTable(appointments, currentIndex, PAGE_SIZE);
            
            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
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
                    
                case "Q":
                    return;
                    
                default:
                    System.out.println("Invalid option. Press Enter to continue...");
                    scanner.nextLine();
            }
        }
    }
    
    private static void displayTable(AppointmentDetail[] appointments, int startIndex, int pageSize) {
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