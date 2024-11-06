package controller;

import java.util.Scanner;

import model.Patient;
import repository.PatientRepository;
import service.PatientInfoService;
import ui.PatientMenuUI;

public class PatientController extends BaseController<PatientMenuUI> {
    private Patient patient;
    private PatientRepository patientRepository;

    private PatientInfoService patientInfoService;
    
    public PatientController(
        Scanner scanner,
        Patient patient,
        PatientRepository patientRepository
    ){
        super(new PatientMenuUI(),scanner);

        this.patient = patient;
        this.patientRepository = patientRepository;
        this.patientInfoService = new PatientInfoService(patientRepository);
    }

    public void handleUserInput() {
        while(true) {
            menu.printOptions();
            String choice = scanner.nextLine();
    
            if(choice.equals("1")) {
                
                return;
            }
            else if(choice.equals("9")) {
                super.handleLogout(this.patient);
                return;
            }
            else {
                System.out.println("Invalid option!");
            }
        }
    }
}
