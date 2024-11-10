package controller;

import java.util.Scanner;

import model.Medicine;
import model.Pharmacist;
import model.ReplenishmentRequestDetail;
import service.AppointmentOutcomeService;
import service.InventoryService;
import service.ReplenishmentRequestService;
import ui.InventoryTableUI;
import ui.PharmacistMenuUI;
import ui.PharmacyPrescriptionUI;
import ui.ReplenishmentRequestTableUI;

public class PharmacistController extends BaseController<PharmacistMenuUI> {
    private Pharmacist pharmacist;
    private InventoryService inventoryService;
    private ReplenishmentRequestService replenishmentRequestService;
    private PharmacyPrescriptionUI prescriptionUI;

    public PharmacistController(
        Scanner scanner,
        Pharmacist pharmacist,
        InventoryService inventoryService,
        ReplenishmentRequestService replenishmentRequestService,
        AppointmentOutcomeService appointmentOutcomeService
    ){
        super(new PharmacistMenuUI(),scanner);
        this.pharmacist = pharmacist;
        this.inventoryService = inventoryService;
        this.replenishmentRequestService = replenishmentRequestService;
        this.prescriptionUI = new PharmacyPrescriptionUI(appointmentOutcomeService);
    }

    public void handleUserInput() {
        while(true) {
            menu.printOptions();
            String choice = scanner.nextLine();
            if(choice.equals("1")){
                prescriptionUI.display(scanner,false);
            } else if(choice.equals("2")){
                prescriptionUI.display(scanner,true);
            } else if(choice.equals("3")) {
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
        ReplenishmentRequestTableUI.display(requests,scanner,false,replenishmentRequestService,inventoryService,this.pharmacist.getId());
    }
    
    private void viewInventory() {
        Medicine[] medicine = inventoryService.getAllMedicines();
        InventoryTableUI.display(medicine, scanner, false, inventoryService);
    }
}