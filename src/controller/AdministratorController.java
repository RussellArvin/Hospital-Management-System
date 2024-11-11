package controller;

import java.util.Scanner;

import enums.AppointmentAction;
import enums.UserRole;
import model.Administrator;
import model.Medicine;
import model.ReplenishmentRequestDetail;
import model.User;
import service.AppointmentOutcomeService;
import service.AppointmentScheduleService;
import service.AppointmentService;
import service.InventoryService;
import service.ReplenishmentRequestService;
import service.StaffService;
import ui.AdministratorMenuUI;
import ui.AppointmentTableUI;
import ui.InventoryTableUI;
import ui.ReplenishmentRequestTableUI;
import ui.StaffTableUI;

public class AdministratorController extends BaseController<AdministratorMenuUI> {
    private Administrator admin;
    private StaffService staffService;
    private InventoryService inventoryService;
    private ReplenishmentRequestService replenishmentRequestService;
    private AppointmentTableUI appointmentTableUI;

    public AdministratorController(
        Scanner scanner,
        Administrator admin,
        ReplenishmentRequestService replenishmentRequestService,
        StaffService staffService,
        InventoryService inventoryService,
        AppointmentService appointmentService,
        AppointmentScheduleService appointmentScheduleService,
        AppointmentOutcomeService appointmentOutcomeService
    ){
        super(new AdministratorMenuUI(), scanner);
        this.admin = admin;
        this.replenishmentRequestService = replenishmentRequestService;
        this.staffService = staffService;
        this.inventoryService = inventoryService;
        this.appointmentTableUI = new AppointmentTableUI(appointmentService, appointmentScheduleService, appointmentOutcomeService, inventoryService, admin, UserRole.ADMINISTRATOR);
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();

            if(choice.equals("1")){
                handleStaffManagement(this.scanner);
            }
            else if(choice.equals("2")){
                appointmentTableUI.display(scanner, AppointmentAction.VIEW);
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
        ReplenishmentRequestTableUI.display(requests,scanner,true,replenishmentRequestService,inventoryService,this.admin.getId());
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
