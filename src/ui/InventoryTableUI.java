package ui;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.Medicine;
import service.InventoryService;

public class InventoryTableUI {

   public static void display(Medicine[] medicines, Scanner scanner, boolean isAdmin, InventoryService inventoryService) {
    final int PAGE_SIZE = 10;
    int currentIndex = 0;
 
    while (true) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        displayTable(medicines, currentIndex, PAGE_SIZE);
        
        System.out.println("\nOptions:");
        System.out.println("N - Next Page");
        System.out.println("P - Previous Page");
        if (isAdmin) {
            System.out.println("A - Add Medicine");
            System.out.println("U - Update Stock");
            System.out.println("R - Remove Medicine");
            System.out.println("L - Update Low Stock Alert");
        }
        System.out.println("Q - Back to Menu");
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine().toUpperCase();
        
        switch (choice) {
            case "N":
                if (currentIndex + PAGE_SIZE < medicines.length) {
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
                    try {
                        System.out.print("Enter medicine name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter initial stock: ");
                        int stock = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter low stock alert level: ");
                        int alertLevel = Integer.parseInt(scanner.nextLine());
                        
                        String error = inventoryService.addMedicine(name, stock, alertLevel);
                        if(error != null) {
                            System.out.println("Error: " + error);
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                        } else {
                            medicines = inventoryService.getAllMedicines();
                            currentIndex = 0;
                        }
                    } catch(NumberFormatException e) {
                        System.out.println("Error: Please enter valid numbers");
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                    }
                }
                break;
 
            case "U":
                if (isAdmin) {
                    try {
                        System.out.print("Enter medicine ID: ");
                        String updateId = scanner.nextLine();
                        System.out.print("Enter new stock level: ");
                        int newStock = Integer.parseInt(scanner.nextLine());
                        
                        String error = inventoryService.updateStock(updateId, newStock);
                        if(error != null) {
                            System.out.println("Error: " + error);
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                        } else {
                            medicines = inventoryService.getAllMedicines();
                        }
                    } catch(NumberFormatException e) {
                        System.out.println("Error: Please enter a valid number for stock");
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                    }
                }
                break;
 
            case "R":
                if (isAdmin) {
                    System.out.print("Enter medicine ID to remove: ");
                    String removeId = scanner.nextLine();
                    System.out.print("Are you sure? (Y/N): ");
                    if (scanner.nextLine().toUpperCase().equals("Y")) {
                        String error = inventoryService.deleteMedicine(removeId);
                        if(error != null) {
                            System.out.println("Error: " + error);
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                        } else {
                            medicines = inventoryService.getAllMedicines();
                            currentIndex = 0;
                        }
                    }
                }
                break;
 
            case "L":
                if (isAdmin) {
                    try {
                        System.out.print("Enter medicine ID: ");
                        String alertId = scanner.nextLine();
                        System.out.print("Enter new low stock alert level: ");
                        int newAlertLevel = Integer.parseInt(scanner.nextLine());
                        
                        String error = inventoryService.updateLowStockAlert(alertId, newAlertLevel);
                        if(error != null) {
                            System.out.println("Error: " + error);
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                        } else {
                            medicines = inventoryService.getAllMedicines();
                        }
                    } catch(NumberFormatException e) {
                        System.out.println("Error: Please enter a valid number for alert level");
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                    }
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
  
   private static void displayTable(Medicine[] medicines, int startIndex, int pageSize) {
        String format = "| %-36s | %-20s | %-7s | %-15s | %-19s | %-19s |%n";
        String separator = "+--------------------------------------+----------------------+---------+-----------------+---------------------+---------------------+%n";

        System.out.format("%n");
        System.out.format(separator);
        System.out.format("| %-130s |%n", "INVENTORY LIST");
        System.out.format(separator);
        System.out.format(format, "ID", "Name", "Stock", "Low Stock Alert", "Created At", "Updated At");
        System.out.format(separator);

        int endIndex = Math.min(startIndex + pageSize, medicines.length);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = startIndex; i < endIndex; i++) {
            Medicine medicine = medicines[i];
            
            System.out.format(format,
                medicine.getId(),
                medicine.getName(),
                medicine.getStock(),
                medicine.getLowStockAlert(),
                medicine.getCreatedAt().format(dateFormat),
                medicine.getUpdatedAt().format(dateFormat)
            );
        }
        System.out.format(separator);

        int currentPage = (startIndex / pageSize) + 1;
        int totalPages = (int) Math.ceil((double) medicines.length / pageSize);
        System.out.format("Page %d of %d (Total records: %d)%n", currentPage, totalPages, medicines.length);
    }

}