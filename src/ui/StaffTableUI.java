package ui;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
        User[] filteredUsers = users; // Keep track of filtered users

        while (true) {
            // Clear console
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            // Display table
            displayTable(filteredUsers, currentIndex, PAGE_SIZE);
            
            // Show options
            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
            System.out.println("U - Update User");
            System.out.println("F - Filter Users");
            System.out.println("R - Reset Filter");
            System.out.println("Q - Back to Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().toUpperCase();
            
            switch (choice) {
                case "N":
                    if (currentIndex + PAGE_SIZE < filteredUsers.length) {
                        currentIndex += PAGE_SIZE;
                    }
                    break;
                    
                case "P":
                    if (currentIndex - PAGE_SIZE >= 0) {
                        currentIndex -= PAGE_SIZE;
                    }
                    break;

                case "U":
                    User selectedUser = promptUserSelection(filteredUsers, scanner);
                    if (selectedUser != null) {
                        return selectedUser;
                    }
                    break;

                case "F":
                    filteredUsers = filterUsers(users, scanner);
                    currentIndex = 0; // Reset to first page after filtering
                    break;

                case "R":
                    filteredUsers = users; // Reset to original array
                    currentIndex = 0;
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

    private static User[] filterUsers(User[] users, Scanner scanner) {
        System.out.println("\nFilter by:");
        System.out.println("1. Role");
        System.out.println("2. Age");
        System.out.println("3. Gender");
        System.out.print("Choose filter option (or press Enter to cancel): ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                return filterByRole(users, scanner);
            case "2":
                return filterByAge(users, scanner);
            case "3":
                return filterByGender(users, scanner);
            default:
                return users;
        }
    }

    private static User[] filterByRole(User[] users, Scanner scanner) {
        System.out.println("\nSelect role:");
        System.out.println("1. Administrator");
        System.out.println("2. Doctor");
        System.out.println("3. Pharmacist");
        System.out.print("Choose role: ");
        
        String choice = scanner.nextLine();
        String targetRole;
        
        switch (choice) {
            case "1":
                targetRole = "Administrator";
                break;
            case "2":
                targetRole = "Doctor";
                break;
            case "3":
                targetRole = "Pharmacist";
                break;
            default:
                return users;
        }
        
        return Arrays.stream(users)
            .filter(user -> determineUserRole(user).equals(targetRole))
            .toArray(User[]::new);
    }

    private static User[] filterByAge(User[] users, Scanner scanner) {
        System.out.print("\nEnter minimum age: ");
        String minAgeStr = scanner.nextLine();
        System.out.print("Enter maximum age: ");
        String maxAgeStr = scanner.nextLine();
        
        try {
            int minAge = minAgeStr.isEmpty() ? 0 : Integer.parseInt(minAgeStr);
            int maxAge = maxAgeStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxAgeStr);
            
            return Arrays.stream(users)
                .filter(user -> user.getAge() >= minAge && user.getAge() <= maxAge)
                .toArray(User[]::new);
        } catch (NumberFormatException e) {
            System.out.println("Invalid age format. Filter cancelled.");
            scanner.nextLine();
            return users;
        }
    }

    private static User[] filterByGender(User[] users, Scanner scanner) {
        System.out.println("\nSelect gender:");
        System.out.println("1. Male");
        System.out.println("2. Female");
        System.out.print("Choose gender: ");
        
        String choice = scanner.nextLine();
        String targetGender;
        
        switch (choice) {
            case "1":
                targetGender = "MALE";
                break;
            case "2":
                targetGender = "FEMALE";
                break;
            default:
                return users;
        }
        
        return Arrays.stream(users)
            .filter(user -> user.getGender().toString().equals(targetGender))
            .toArray(User[]::new);
    }
}