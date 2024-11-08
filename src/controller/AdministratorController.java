package controller;

import java.util.Scanner;

import model.Administrator;
import model.Medicine;
import model.ReplenishmentRequestDetail;
import model.User;
import repository.AdministratorRepository;
import repository.DoctorRepository;
import repository.MedicineRepository;
import repository.PharmacistRepository;
import repository.ReplenishmentRequestRepository;
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
    private MedicineRepository medicineRepository;
    private AdministratorRepository administratorRepository;
    private DoctorRepository doctorRepository;
    private PharmacistRepository pharmacistRepository;

    public AdministratorController(
        Scanner scanner,
        Administrator admin,
        AdministratorRepository administratorRepository,
        DoctorRepository doctorRepository,
        PharmacistRepository pharmacistRepository
    ){
        super(new AdministratorMenuUI(), scanner);
        this.admin = admin;
        this.medicineRepository = new MedicineRepository();
        this.replenishmentRequestService = new ReplenishmentRequestService(new ReplenishmentRequestRepository(), this.medicineRepository, pharmacistRepository);
        this.staffService = new StaffService(doctorRepository, pharmacistRepository, administratorRepository);
        this.administratorRepository = administratorRepository;
        this.doctorRepository = doctorRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.inventoryService = new InventoryService(medicineRepository);
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
        Medicine[] medicine = this.medicineRepository.findAll();
        InventoryTableUI.display(medicine, scanner, true, inventoryService);
    }

}
