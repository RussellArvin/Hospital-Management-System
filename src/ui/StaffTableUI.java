package ui;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import model.User;
import model.Administrator;
import model.Doctor;
import model.Pharmacist;
import service.StaffService;
import enums.Gender;
import enums.UserRole;

public class StaffTableUI {

    
    public static void display(User[] users, Scanner scanner, StaffService staffService) {
        final int PAGE_SIZE = 10;
        int currentIndex = 0;
        User[] filteredUsers = users;
        
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            displayTable(filteredUsers, currentIndex, PAGE_SIZE);
            
            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
            System.out.println("A - Add User");
            System.out.println("U - Update User");
            System.out.println("R - Remove User");
            System.out.println("F - Filter Users");
            System.out.println("C - Clear Filter");
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
                case "A":
                    try {
                        System.out.println("\nSelect user type:");
                        System.out.println("1. " + UserRole.ADMINISTRATOR);
                        System.out.println("2. " + UserRole.DOCTOR);
                        System.out.println("3. " + UserRole.PHARMACIST);
                        System.out.print("Choose type: ");
                        
                        String typeChoice = scanner.nextLine();
                        UserRole role;
                        
                        switch(typeChoice) {
                            case "1":
                                role = UserRole.ADMINISTRATOR;
                                break;
                            case "2":
                                role = UserRole.DOCTOR;
                                break;
                            case "3":
                                role = UserRole.PHARMACIST;
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid user type");
                        }

                        String id;
                        while (true) {
                            System.out.print("Enter ID: ");
                            id = scanner.nextLine().trim();
                            
                            if (id.isEmpty()) {
                                System.out.println("Error: ID cannot be empty. Press Enter to try again.");
                                scanner.nextLine();
                                continue;
                            }

                            if (staffService.isDuplicateId(id)) {
                                System.out.println("Error: ID '" + id + "' is already taken. Press Enter to try again.");
                                scanner.nextLine();
                                continue;
                            }

                            break;
                        }
                        
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        
                        System.out.print("Enter age: ");
                        int age = Integer.parseInt(scanner.nextLine());
                        
                        // Gender selection using enum
                        System.out.println("Select gender:");
                        System.out.println("1. " + Gender.MALE);
                        System.out.println("2. " + Gender.FEMALE);
                        System.out.print("Choose gender (1/2): ");
                        
                        Gender gender;
                        String genderChoice = scanner.nextLine();
                        switch (genderChoice) {
                            case "1":
                                gender = Gender.MALE;
                                break;
                            case "2":
                                gender = Gender.FEMALE;
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid gender selection");
                        }
                        
                        String error = staffService.addUser(id, role, name, age, gender);
                        if(error != null) {
                            System.out.println("Error: " + error);
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                        } else {
                            filteredUsers = users = staffService.getAllStaffData();
                            currentIndex = 0;
                        }
                    } catch(NumberFormatException e) {
                        System.out.println("Error: Please enter valid numbers");
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                    } catch(IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                    }
                    break;
                case "R":
                    System.out.print("Enter user ID to remove: ");
                    String removeId = scanner.nextLine();
                    System.out.print("Are you sure? (Y/N): ");
                    if (scanner.nextLine().toUpperCase().equals("Y")) {
                        String error = staffService.removeUser(removeId);
                        if(error != null) {
                            System.out.println("Error: " + error);
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                        } else {
                            filteredUsers = users = staffService.getAllStaffData();
                            currentIndex = 0;
                        }
                    }
                    break;

                case "U":
                    System.out.print("\nEnter user ID to update: ");
                    String userId = scanner.nextLine();
                    
                    System.out.println("\nSelect field to update:");
                    System.out.println("1. Name");
                    System.out.println("2. Age");
                    System.out.println("3. Password");
                    System.out.print("Choose option: ");
                    String updateChoice = scanner.nextLine();

                    try {
                        String error = null;
                        switch(updateChoice) {
                            case "1":
                                System.out.print("Enter new name: ");
                                String newName = scanner.nextLine();
                                error = staffService.updateUserName(userId, newName);
                                break;
                            case "2":
                                System.out.print("Enter new age: ");
                                int newAge = Integer.parseInt(scanner.nextLine());
                                error = staffService.updateUserAge(userId, newAge);
                                break;
                            case "3":
                                System.out.print("Enter new password: ");
                                String newPassword = scanner.nextLine();
                                error = staffService.updateUserPassword(userId, newPassword);
                                break;
                            default:
                                System.out.println("Invalid option");
                                continue;
                        }

                        if(error != null) {
                            System.out.println("Error: " + error);
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                        } else {
                            filteredUsers = users = staffService.getAllStaffData();
                        }
                    } catch(NumberFormatException e) {
                        System.out.println("Error: Please enter valid numbers");
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                    }
                    break;

                case "F":
                    filteredUsers = filterUsers(users, scanner);
                    currentIndex = 0;
                    break;

                case "C":
                    filteredUsers = users;
                    currentIndex = 0;
                    break;
                    
                case "Q":
                    return;
                    
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