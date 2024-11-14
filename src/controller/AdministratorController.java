package controller;

import enums.AppointmentAction;
import enums.UserRole;
import java.util.Scanner;
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

/**
 * The AdministratorController class handles the administrator's actions and interactions 
 * within the system, providing access to staff management, inventory, appointments, and 
 * replenishment requests. Extends the BaseController with an Administrator-specific UI.
 * 
 * @author Russell Arvin
 */
public class AdministratorController extends BaseController<AdministratorMenuUI> {

    private Administrator admin;
    private StaffService staffService;
    private InventoryService inventoryService;
    private ReplenishmentRequestService replenishmentRequestService;
    private AppointmentTableUI appointmentTableUI;

    /**
     * Constructs an AdministratorController object.
     *
     * @param scanner The Scanner object used for reading user input.
     * @param admin The Administrator model associated with this controller.
     * @param replenishmentRequestService The service used for handling replenishment requests.
     * @param staffService The service used for managing staff data.
     * @param inventoryService The service used for managing the inventory.
     * @param appointmentService The service used for managing appointments.
     * @param appointmentScheduleService The service used for managing appointment schedules.
     * @param appointmentOutcomeService The service used for handling appointment outcomes.
     */
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

    /**
     * Handles the administrator's input and directs actions based on the user's choices.
     * Options include staff management, appointment viewing, inventory management, 
     * replenishment requests, and logout.
     */
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

    /**
     * Retrieves and displays the list of replenishment requests for the administrator to review.
     */
    private void handleReplenishmentRequests(){
        ReplenishmentRequestDetail[] requests = replenishmentRequestService.getRequests();
        ReplenishmentRequestTableUI.display(requests,scanner,true,replenishmentRequestService,inventoryService,this.admin.getId());
    }

    /**
     * Retrieves and displays the list of all staff members in the system.
     *
     * @param scanner The Scanner object used for reading user input.
     */
    private void handleStaffManagement(Scanner scanner) {
        User[] staff = this.staffService.getAllStaffData();
        
        StaffTableUI.display(staff, scanner, staffService);
    }

    /**
     * Retrieves and displays the inventory of all medicines in the system.
     */
    private void viewInventory() {
        Medicine[] medicine = inventoryService.getAllMedicines();
        InventoryTableUI.display(medicine, scanner, true, inventoryService);
    }

}
