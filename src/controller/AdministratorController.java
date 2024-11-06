package controller;

import java.util.Scanner;

import model.Administrator;
import repository.AdministratorRepository;
import ui.AdministratorMenuUI;

public class AdministratorController extends BaseController<AdministratorMenuUI> {
    private Administrator admin;
    private AdministratorRepository administratorRepository;

    public AdministratorController(
        Scanner scanner,
        Administrator admin,
        AdministratorRepository administratorRepository
    ){
        super(new AdministratorMenuUI(), scanner);
        this.admin = admin;
        this.administratorRepository = administratorRepository;
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();
        }
    }
}
