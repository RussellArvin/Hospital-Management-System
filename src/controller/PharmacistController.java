package controller;

import java.util.Scanner;

import model.Medicine;
import model.Pharmacist;
import model.ReplenishmentRequestDetail;
import service.InventoryService;
import service.ReplenishmentRequestService;
import ui.InventoryTableUI;
import ui.PharmacistMenuUI;
import ui.ReplenishmentRequestTableUI;

public class PharmacistController extends BaseController<PharmacistMenuUI> {
    private Pharmacist pharmacist;
    private InventoryService inventoryService;
    private ReplenishmentRequestService replenishmentRequestService;

    public PharmacistController(
        Scanner scanner,
        Pharmacist pharmacist,
        InventoryService inventoryService,
        ReplenishmentRequestService replenishmentRequestService
    ){
        super(new PharmacistMenuUI(),scanner);
        this.pharmacist = pharmacist;
        this.inventoryService = inventoryService;
        this.replenishmentRequestService = replenishmentRequestService;
    }

    public void handleUserInput() {
        while(true) {
            menu.printOptions();
            String choice = scanner.nextLine();
            
            if(choice.equals("3")) {
                viewInventory();
            } else if(choice.equals("4")){
                 handleReplenishmentRequests();
            } else if(choice.equals("5")) {
                super.handleLogout(pharmacist);
                return; // Add return to exit after logout
            } else {
                super.invalidOption();
            }
            // Remove the inner while(true) loop
        }
    }

    private void handleReplenishmentRequests(){
        ReplenishmentRequestDetail[] requests = replenishmentRequestService.getPharmacistRequests(this.pharmacist.getId());
        ReplenishmentRequestTableUI.display(requests,scanner,false,replenishmentRequestService,this.pharmacist.getId());
    }
    
    private void viewInventory() {
        Medicine[] medicine = inventoryService.getAllMedicines();
        InventoryTableUI.display(medicine, scanner, false, inventoryService);
    }
}