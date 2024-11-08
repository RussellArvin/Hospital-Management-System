package controller;

import java.util.Scanner;

import model.Medicine;
import model.Pharmacist;
import model.ReplenishmentRequestDetail;
import repository.MedicineRepository;
import repository.PharmacistRepository;
import repository.ReplenishmentRequestRepository;
import service.InventoryService;
import service.ReplenishmentRequestService;
import ui.InventoryTableUI;
import ui.PharmacistMenuUI;
import ui.ReplenishmentRequestTableUI;

public class PharmacistController extends BaseController<PharmacistMenuUI> {
    private Pharmacist pharmacist;

    private PharmacistRepository pharmacistRepository;
    private MedicineRepository medicineRepository;
    private ReplenishmentRequestRepository replenishmentRequestRepository;
    private InventoryService inventoryService;
    private ReplenishmentRequestService replenishmentRequestService;

    public PharmacistController(
        Scanner scanner,
        Pharmacist pharmacist,
        PharmacistRepository  pharmacistRepository,
        MedicineRepository medicineRepository
    ){
        super(new PharmacistMenuUI(),scanner);
        this.pharmacist = pharmacist;
        this.pharmacistRepository = pharmacistRepository;
        this.medicineRepository = medicineRepository;
        this.replenishmentRequestRepository = new ReplenishmentRequestRepository();
        this.inventoryService = new InventoryService(medicineRepository);
        this.replenishmentRequestService = new ReplenishmentRequestService(this.replenishmentRequestRepository, medicineRepository, pharmacistRepository);
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
        Medicine[] medicine = this.medicineRepository.findAll();
        InventoryTableUI.display(medicine, scanner, false, inventoryService);
    }
}