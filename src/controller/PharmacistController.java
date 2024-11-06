package controller;

import java.util.Scanner;

import model.Pharmacist;
import repository.PharmacistRepository;
import ui.PharmacistMenuUI;

public class PharmacistController extends BaseController<PharmacistMenuUI> {
    private Pharmacist pharmacist;

    private PharmacistRepository pharmacistRepository;

    public PharmacistController(
        Scanner scanner,
        Pharmacist pharmacist,
        PharmacistRepository  pharmacistRepository
    ){
        super(new PharmacistMenuUI(),scanner);
        this.pharmacist = pharmacist;
        this.pharmacistRepository = pharmacistRepository;
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();
        }
    }
}
