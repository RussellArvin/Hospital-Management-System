package controller;

import java.util.Scanner;

import model.Administrator;
import model.User;
import repository.AdministratorRepository;
import repository.DoctorRepository;
import repository.PharmacistRepository;
import service.StaffService;
import ui.AdministratorMenuUI;
import ui.StaffTableUI;

public class AdministratorController extends BaseController<AdministratorMenuUI> {
    private Administrator admin;
    private StaffService staffService;
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
        this.staffService = new StaffService(doctorRepository, pharmacistRepository, administratorRepository);
        this.administratorRepository = administratorRepository;
        this.doctorRepository = doctorRepository;
        this.pharmacistRepository = pharmacistRepository;
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();

            if(choice.equals("1")){
                handleStaffManagement(this.scanner);
            }
            else if(choice.equals("5")){
                super.handleLogout((admin));
                break;
            }
        }
    }

    private void handleStaffManagement(Scanner scanner) {
        User[] staff = this.staffService.getAllStaffData();
        
        while (true) {
            User selectedUser = StaffTableUI.display(staff, scanner);
            
            if (selectedUser == null) {
                // User chose to quit
                return;
            }
    
            // Handle user update
            boolean updated = true;
            if (updated) {
                // Refresh staff data after update
                staff = this.staffService.getAllStaffData();
            }
        }
    }

}
