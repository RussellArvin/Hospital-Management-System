package controller;

import enums.AppointmentAction;
import enums.UserRole;
import java.util.Scanner;
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
 * The AdministratorController class is responsible for managing the actions available to the administrator
 * within the system. It interacts with various services and user interfaces to provide functionality
 * such as managing staff, viewing appointments, handling inventory, and processing replenishment requests.
 * This controller is designed to handle user input and direct the flow of the administrator's actions
 * within the system.
 * 
 * @author Russel Arvin
 * @version 1.0
 */
public class AdministratorController extends BaseController<AdministratorMenuUI> {

    private Administrator admin;
    private StaffService staffService;
    private InventoryService inventoryService;
    private ReplenishmentRequestService replenishmentRequestService;
    private AppointmentTableUI appointmentTableUI;

    /**
     * Constructs an AdministratorController with the necessary services and administrator details.
     * 
     * @param scanner the Scanner object used to get user input
     * @param admin the administrator object managing the system
     * @param replenishmentRequestService the service responsible for managing replenishment requests
     * @param staffService the service responsible for managing staff data
     * @param inventoryService the service responsible for managing the inventory
     * @param appointmentService the service responsible for managing appointments
     * @param appointmentScheduleService the service responsible for scheduling appointments
     * @param appointmentOutcomeService the service responsible for managing appointment outcomes
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
     * Handles the user input by continuously displaying the menu options and processing the selected actions.
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
     * Handles replenishment requests by retrieving and displaying the current requests.
     */
    private void handleReplenishmentRequests(){
        ReplenishmentRequestDetail[] requests = replenishmentRequestService.getRequests();
        ReplenishmentRequestTableUI.display(requests,scanner,true,replenishmentRequestService,inventoryService,this.admin.getId());
    }

    /**
     * Handles the management of staff data by displaying the list of all staff members.
     * 
     * @param scanner the Scanner object used to get user input
     */
    private void handleStaffManagement(Scanner scanner) {
        User[] staff = this.staffService.getAllStaffData();
        
        StaffTableUI.display(staff, scanner, staffService);
    }

    /**
     * Displays the current inventory of medicines available in the system.
     */
    private void viewInventory() {
        Medicine[] medicine = inventoryService.getAllMedicines();
        InventoryTableUI.display(medicine, scanner, true, inventoryService);
    }

}
