package ui;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.ReplenishmentRequestDetail;
import service.ReplenishmentRequestService;
import enums.ReplenishmentRequestStatus;

public class ReplenishmentRequestTableUI {
    private static final int COLUMNS = 7; // ID, Medicine Name, Pharmacist Name, New Amount, Status, Created At, Updated At
    
    public static void display(ReplenishmentRequestDetail[] requests, Scanner scanner, boolean isAdmin, ReplenishmentRequestService requestService) {
        final int PAGE_SIZE = 10;
        int currentIndex = 0;
        
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            displayTable(requests, currentIndex, PAGE_SIZE);
            
            System.out.println("\nOptions:");
            System.out.println("N - Next Page");
            System.out.println("P - Previous Page");
            if (isAdmin) {
                System.out.println("A - Approve Request");
                System.out.println("R - Reject Request");
            }
            System.out.println("Q - Back to Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().toUpperCase();
            
            switch (choice) {
                case "N":
                    if (currentIndex + PAGE_SIZE < requests.length) {
                        currentIndex += PAGE_SIZE;
                    }
                    break;
                    
                case "P":
                    if (currentIndex - PAGE_SIZE >= 0) {
                        currentIndex -= PAGE_SIZE;
                    }
                    break;
                    
                // case "A":
                //     if (isAdmin) {
                //         System.out.print("Enter request ID to approve: ");
                //         String approveId = scanner.nextLine();
                        
                //         String error = requestService.updateRequestStatus(approveId, ReplenishmentRequestStatus.APPROVED);
                //         if(error != null) {
                //             System.out.println("Error: " + error);
                //             System.out.println("Press Enter to continue...");
                //             scanner.nextLine();
                //         } else {
                //             requests = requestService.getRequests();
                //         }
                //     }
                //     break;
                    
                // case "R":
                //     if (isAdmin) {
                //         System.out.print("Enter request ID to reject: ");
                //         String rejectId = scanner.nextLine();
                        
                //         String error = requestService.updateRequestStatus(rejectId, ReplenishmentRequestStatus.REJECTED);
                //         if(error != null) {
                //             System.out.println("Error: " + error);
                //             System.out.println("Press Enter to continue...");
                //             scanner.nextLine();
                //         } else {
                //             requests = requestService.getRequests();
                //         }
                //     }
                //     break;
                    
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