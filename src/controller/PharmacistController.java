package controller;

import java.util.Scanner;

import model.Medicine;
import model.Pharmacist;
import repository.MedicineRepository;
import repository.PharmacistRepository;
import service.InventoryService;
import ui.InventoryTableUI;
import ui.PharmacistMenuUI;

public class PharmacistController extends BaseController<PharmacistMenuUI> {
    private Pharmacist pharmacist;

    private PharmacistRepository pharmacistRepository;
    private MedicineRepository medicineRepository;
    private InventoryService inventoryService;

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
        this.inventoryService = new InventoryService(medicineRepository);
    }

    public void handleUserInput() {
        while(true) {
            menu.printOptions();
            String choice = scanner.nextLine();
            
            if(choice.equals("3")) {
                viewInventory();
            } else if(choice.equals("5")) {
                super.handleLogout(pharmacist);
                return; // Add return to exit after logout
            } else {
                super.invalidOption();
            }
            // Remove the inner while(true) loop
        }
    }
    
    private void viewInventory() {
        Medicine[] medicine = this.medicineRepository.findAll();
        InventoryTableUI.display(medicine, scanner, false, inventoryService);
    }
}