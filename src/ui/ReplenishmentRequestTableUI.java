package ui;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import model.ReplenishmentRequestDetail;
import service.InventoryService;
import service.ReplenishmentRequestService;
import enums.ReplenishmentRequestStatus;

public class ReplenishmentRequestTableUI {
    
    public static void display(
        ReplenishmentRequestDetail[] requests, 
        Scanner scanner, 
        boolean isAdmin, 
        ReplenishmentRequestService requestService, 
        InventoryService inventoryService,
        String userId
    ) {
        final int PAGE_SIZE = 10;
        int currentIndex = 0;
        ReplenishmentRequestDetail[] filteredRequests = requests;
        
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            displayTable(filteredRequests, currentIndex, PAGE_SIZE);
            
            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
            if (isAdmin) {
                System.out.println("A - Approve Request");
                System.out.println("R - Reject Request");
            } else {
                System.out.println("C - Create New Request");
                System.out.println("F - Filter Requests");
                System.out.println("X - Clear Filter");
            }
            System.out.println("Q - Back to Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().toUpperCase();
            
            switch (choice) {
                case "N":
                    if (currentIndex + PAGE_SIZE < filteredRequests.length) {
                        currentIndex += PAGE_SIZE;
                    }
                    break;
                    
                case "P":
                    if (currentIndex - PAGE_SIZE >= 0) {
                        currentIndex -= PAGE_SIZE;
                    }
                    break;
                    
                case "A":
                    if (isAdmin) {
                        System.out.print("Enter request ID to approve: ");
                        String approveId = scanner.nextLine();
                        
                        String error = requestService.updateRequestStatus(approveId, ReplenishmentRequestStatus.APPROVED);
                        if(error != null) {
                            System.out.println("Error: " + error);
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                        } else {
                            filteredRequests = requests = requestService.getRequests();
                        }
                    }
                    break;
                    
                case "R":
                    if (isAdmin) {
                        System.out.print("Enter request ID to reject: ");
                        String rejectId = scanner.nextLine();
                        
                        String error = requestService.updateRequestStatus(rejectId, ReplenishmentRequestStatus.REJECTED);
                        if(error != null) {
                            System.out.println("Error: " + error);
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                        } else {
                            filteredRequests = requests = requestService.getRequests();
                        }
                    }
                    break;

                case "C":
                    if (!isAdmin) {
                        createRequest(requestService, inventoryService, scanner, userId);
                        // Refresh requests list after creation
                        filteredRequests = requests = requestService.getPharmacistRequests(userId);
                    }
                    break;

                case "F":
                    if (!isAdmin) {
                        filteredRequests = filterByStatus(requests, scanner);
                        currentIndex = 0;
                    }
                    break;

                case "X":
                    if (!isAdmin) {
                        filteredRequests = requests;
                        currentIndex = 0;
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
    
    private static void displayTable(ReplenishmentRequestDetail[] requests, int startIndex, int pageSize) {
        String format = "| %-36s | %-20s | %-20s | %-11s | %-10s | %-19s | %-19s |%n";
        String separator = "+--------------------------------------+----------------------+----------------------+-------------+------------+---------------------+---------------------+%n";

        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| %-150s |%n", "REPLENISHMENT REQUESTS");
        System.out.format(separator);
        System.out.format(format, 
            "ID", 
            "Medicine Name", 
            "Pharmacist Name", 
            "New Amount", 
            "Status", 
            "Created At", 
            "Updated At"
        );
        System.out.format(separator);

        if (requests.length == 0) {
            System.out.format("| %-150s |%n", "No requests found");
            System.out.format(separator);
        } else {
            int endIndex = Math.min(startIndex + pageSize, requests.length);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (int i = startIndex; i < endIndex; i++) {
                ReplenishmentRequestDetail request = requests[i];
                
                System.out.format(format,
                    request.getId(),
                    request.getMedicine().getName(),
                    request.getPharmacist().getName(),
                    request.getNewAmount(),
                    request.getStatus(),
                    request.getCreatedAt().format(dateFormat),
                    request.getUpdatedAt().format(dateFormat)
                );
            }
            System.out.format(separator);
            
            int currentPage = (startIndex / pageSize) + 1;
            int totalPages = (int) Math.ceil((double) requests.length / pageSize);
            System.out.format("Page %d of %d (Total records: %d)%n", currentPage, totalPages, requests.length);
        }
    }

    private static ReplenishmentRequestDetail[] filterByStatus(ReplenishmentRequestDetail[] requests, Scanner scanner) {
        System.out.println("\nSelect status to filter by:");
        System.out.println("1. " + ReplenishmentRequestStatus.PENDING);
        System.out.println("2. " + ReplenishmentRequestStatus.APPROVED);
        System.out.println("3. " + ReplenishmentRequestStatus.REJECTED);
        System.out.print("Choose status (or press Enter to cancel): ");
        
        String choice = scanner.nextLine();
        ReplenishmentRequestStatus targetStatus;
        
        switch (choice) {
            case "1":
                targetStatus = ReplenishmentRequestStatus.PENDING;
                break;
            case "2":
                targetStatus = ReplenishmentRequestStatus.APPROVED;
                break;
            case "3":
                targetStatus = ReplenishmentRequestStatus.REJECTED;
                break;
            default:
                return requests;
        }
        
        return Arrays.stream(requests)
            .filter(request -> request.getStatus() == targetStatus)
            .toArray(ReplenishmentRequestDetail[]::new);
    }
    
    private static void createRequest(ReplenishmentRequestService requestService, InventoryService inventoryService, Scanner scanner, String userId) {
        try {
            // Get medicine name from user
            System.out.print("\nEnter medicine name: ");
            String medicineName = scanner.nextLine().trim();
            
            if (medicineName.isEmpty()) {
                System.out.println("Error: Medicine name cannot be empty");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }
            
            // Get medicine ID from service
            String medicineId = inventoryService.getMedicineId(medicineName);
            if (medicineId == null) {
                System.out.println("Error: Medicine not found");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }
            
            // Get new amount from user
            System.out.print("Enter requested amount: ");
            int newAmount;
            try {
                String amountStr = scanner.nextLine().trim();
                if (amountStr.isEmpty()) {
                    System.out.println("Error: Amount cannot be empty");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    return;
                }
                
                newAmount = Integer.parseInt(amountStr);
                if (newAmount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid positive number");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }
            
            // Confirm request
            System.out.println("\nRequest Summary:");
            System.out.println("Medicine: " + medicineName);
            System.out.println("Amount: " + newAmount);
            System.out.print("Confirm request? (Y/N): ");
            
            if (scanner.nextLine().toUpperCase().equals("Y")) {
                // Create the request
                String error = requestService.createRequest(medicineId, userId, newAmount);
                
                if (error != null) {
                    System.out.println("Error: " + error);
                } else {
                    System.out.println("Request created successfully!");
                }
            } else {
                System.out.println("Request cancelled.");
            }
            
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            
        } catch (Exception e) {
            System.out.println("Error: An unexpected error occurred");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }
}