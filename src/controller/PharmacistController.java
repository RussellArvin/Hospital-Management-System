package controller;

import java.util.Scanner;
import model.Medicine;
import model.Pharmacist;
import model.ReplenishmentRequestDetail;
import service.AppointmentOutcomeService;
import service.InventoryService;
import service.ReplenishmentRequestService;
import ui.AppointmentOutcomeTableUI;
import ui.InventoryTableUI;
import ui.PharmacistMenuUI;
import ui.ReplenishmentRequestTableUI;

/**
 * Controller for handling pharmacist-related actions and interactions.
 * Provides functionalities for managing inventory, viewing appointment outcomes,
 * and handling replenishment requests.
 * 
 * @author Lim Jun Howe
 * @version 1.0
 */
public class PharmacistController extends BaseController<PharmacistMenuUI> {
    private Pharmacist pharmacist;  // Pharmacist associated with this controller
    private InventoryService inventoryService;  // Service for managing inventory data
    private ReplenishmentRequestService replenishmentRequestService;  // Service for handling replenishment requests
    private AppointmentOutcomeTableUI outcomeUI;  // UI for displaying appointment outcomes

    /**
     * Constructs a PharmacistController with the specified scanner, pharmacist, and various services for handling
     * inventory, replenishment requests, and appointment outcomes.
     *
     * @param scanner the Scanner instance for reading user input
     * @param pharmacist the Pharmacist associated with this controller
     * @param inventoryService the service for managing inventory data
     * @param replenishmentRequestService the service for handling replenishment requests
     * @param appointmentOutcomeService the service for managing appointment outcomes
     */
    public PharmacistController(
        Scanner scanner,
        Pharmacist pharmacist,
        InventoryService inventoryService,
        ReplenishmentRequestService replenishmentRequestService,
        AppointmentOutcomeService appointmentOutcomeService
    ) {
        super(new PharmacistMenuUI(), scanner);
        this.pharmacist = pharmacist;
        this.inventoryService = inventoryService;
        this.replenishmentRequestService = replenishmentRequestService;
        this.outcomeUI = new AppointmentOutcomeTableUI(appointmentOutcomeService, null);
    }

    /**
     * Main loop for handling pharmacist user input, providing options to view appointment outcomes,
     * view inventory, and manage replenishment requests.
     */
    public void handleUserInput() {
        while (true) {
            menu.printOptions();
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                outcomeUI.display(scanner, false);
            } else if (choice.equals("2")) {
                outcomeUI.display(scanner, true);
            } else if (choice.equals("3")) {
                viewInventory();
            } else if (choice.equals("4")) {
                handleReplenishmentRequests();
            } else if (choice.equals("5")) {
                super.handleLogout(pharmacist);
                return;
            } else {
                super.invalidOption();
            }
        }
    }

    /**
     * Handles the viewing and processing of replenishment requests by the pharmacist.
     * Displays the list of requests and allows interaction with the replenishment system.
     */
    private void handleReplenishmentRequests() {
        ReplenishmentRequestDetail[] requests = replenishmentRequestService.getPharmacistRequests(this.pharmacist.getId());
        ReplenishmentRequestTableUI.display(requests, scanner, false, replenishmentRequestService, inventoryService, this.pharmacist.getId());
    }

    /**
     * Displays the current inventory of medicines, allowing the pharmacist to view
     * available stock and manage inventory details.
     */
    private void viewInventory() {
        Medicine[] medicine = inventoryService.getAllMedicines();
        InventoryTableUI.display(medicine, scanner, false, inventoryService);
    }
}
