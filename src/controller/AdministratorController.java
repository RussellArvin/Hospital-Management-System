package controller;

import java.util.Scanner;

import model.Administrator;
import model.Medicine;
import model.ReplenishmentRequestDetail;
import model.User;
import service.InventoryService;
import service.ReplenishmentRequestService;
import service.StaffService;
import ui.AdministratorMenuUI;
import ui.InventoryTableUI;
import ui.ReplenishmentRequestTableUI;
import ui.StaffTableUI;

public class AdministratorController extends BaseController<AdministratorMenuUI> {
    private Administrator admin;
    private StaffService staffService;
    private InventoryService inventoryService;
    private ReplenishmentRequestService replenishmentRequestService;

    public AdministratorController(
        Scanner scanner,
        Administrator admin,
        ReplenishmentRequestService replenishmentRequestService,
        StaffService staffService,
        InventoryService inventoryService
    ){
        super(new AdministratorMenuUI(), scanner);
        this.admin = admin;
        this.replenishmentRequestService = replenishmentRequestService;
        this.staffService = staffService;
        this.inventoryService = inventoryService;
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();

            if(choice.equals("1")){
                handleStaffManagement(this.scanner);
            }
            else if(choice.equals("3")){
                viewInventory();
            }
            else if(choice.equals("4")){
                handleReplenishmentRequests();
            }
            else if(choice.equals("5")){
                super.handleLogout((admin));
                break;
            }
        }
    }

    private void handleReplenishmentRequests(){
        ReplenishmentRequestDetail[] requests = replenishmentRequestService.getRequests();
        ReplenishmentRequestTableUI.display(requests,scanner,true,replenishmentRequestService,this.admin.getId());
    }

    private void handleStaffManagement(Scanner scanner) {
        User[] staff = this.staffService.getAllStaffData();
        
        StaffTableUI.display(staff, scanner, staffService);
    }

    private void viewInventory() {
        Medicine[] medicine = inventoryService.getAllMedicines();
        InventoryTableUI.display(medicine, scanner, true, inventoryService);
    }

}
