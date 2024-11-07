package ui;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.User;
import model.Administrator;
import model.Doctor;
import model.Pharmacist;

public class StaffTableUI {
    private static final int COLUMNS = 7; // ID, Name, Age, Gender, Role, Created At, Updated At
    
    public static User display(User[] users, Scanner scanner) {
        final int PAGE_SIZE = 10;
        int currentIndex = 0;

        while (true) {
            // Clear console
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            // Display table
            displayTable(users, currentIndex, PAGE_SIZE);
            
            // Show options
            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
            System.out.println("U - Update User");
            System.out.println("Q - Back to Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().toUpperCase();
            
            switch (choice) {
                case "N":
                    if (currentIndex + PAGE_SIZE < users.length) {
                        currentIndex += PAGE_SIZE;
                    }
                    break;
                    
                case "P":
                    if (currentIndex - PAGE_SIZE >= 0) {
                        currentIndex -= PAGE_SIZE;
                    }
                    break;

                case "U":
                    User selectedUser = promptUserSelection(users, scanner);
                    if (selectedUser != null) {
                        return selectedUser;
                    }
                    break;
                    
                case "Q":
                    return null;
                    
                default:
                    System.out.println("Invalid option. Press Enter to continue...");
                    scanner.nextLine();
            }
        }
    }

    private static void displayTable(User[] users, int startIndex, int pageSize) {
        String format = "| %-10s | %-20s | %-5s | %-8s | %-15s | %-19s | %-19s |%n";
        String separator = "+------------+----------------------+-------+----------+-----------------+---------------------+---------------------+%n";

        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| STAFF LIST                                                                                                    |%n");
        System.out.format(separator);
        System.out.format(format, "ID", "Name", "Age", "Gender", "Role", "Created At", "Updated At");
        System.out.format(separator);

        int endIndex = Math.min(startIndex + pageSize, users.length);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = startIndex; i < endIndex; i++) {
            User user = users[i];
            String role = determineUserRole(user);
            
            System.out.format(format,
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                role,
                user.getCreatedAt().format(dateFormat),
                user.getUpdatedAt().format(dateFormat)
            );
        }
        System.out.format(separator);
        
        int currentPage = (startIndex / pageSize) + 1;
        int totalPages = (int) Math.ceil((double) users.length / pageSize);
        System.out.format("Page %d of %d (Total records: %d)%n", currentPage, totalPages, users.length);
    }

    private static String determineUserRole(User user) {
        if (user instanceof Administrator) {
            return "Administrator";
        } else if (user instanceof Doctor) {
            return "Doctor";
        } else if (user instanceof Pharmacist) {
            return "Pharmacist";
        }
        return "Unknown";
    }

    private static User promptUserSelection(User[] users, Scanner scanner) {
        System.out.print("\nEnter the ID of the user to update (or press Enter to cancel): ");
        String selectedId = scanner.nextLine().trim();
        
        if (selectedId.isEmpty()) {
            return null;
        }

        // Find user with matching ID
        for (User user : users) {
            if (user.getId().equals(selectedId)) {
                System.out.println("\nSelected user: " + user.getName() + " (" + determineUserRole(user) + ")");
                return user;
            }
        }

        System.out.println("User not found. Press Enter to continue...");
        scanner.nextLine();
        return null;
    }
}