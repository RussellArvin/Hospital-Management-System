package controller;

import java.util.Scanner;

import model.Patient;
import repository.PatientRepository;
import service.MedicalRecordService;
import ui.MedicalRecordUI;
import ui.PatientMenuUI;
import validator.InputValidator;

public class PatientController extends BaseController<PatientMenuUI> {
    private Patient patient;
    private PatientRepository patientRepository;

    private MedicalRecordService medicalRecordService;
    
    public PatientController(
        Scanner scanner,
        Patient patient,
        PatientRepository patientRepository
    ){
        super(new PatientMenuUI(),scanner);

        this.patient = patient;
        this.patientRepository = patientRepository;
        this.medicalRecordService= new MedicalRecordService(this.patientRepository);
    }

    public void handleUserInput() {
        while(true) {
            menu.printOptions();
            String choice = scanner.nextLine();
    
            if(choice.equals("1")) {
                MedicalRecordUI.display(this.patient, this.scanner);
            }
            else if(choice.equals("2")){
                updatePersonalInformation();
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

    public void updatePersonalInformation() {
        String phoneNumber;
        String email;
        
        while (true) {
            System.out.print("\nEnter new phone number (or press Enter to skip): ");
            phoneNumber = scanner.nextLine().trim();
            
            if (phoneNumber.isEmpty()) {
                break;
            }
            
            if (InputValidator.validatePhone(phoneNumber)) {
                break;
            }
            System.out.println("Invalid phone number! Please try again.");
        }
        
        while (true) {
            System.out.print("Enter new email (or press Enter to skip): ");
            email = scanner.nextLine().trim();
            
            if (email.isEmpty()) {
                break;
            }
            
            if (InputValidator.validateEmail(email)) {
                break;
            }
            System.out.println("Invalid email address! Please try again.");
        }
        
        // Only update if at least one field was entered
        if (!phoneNumber.isEmpty() || !email.isEmpty()) {
            medicalRecordService.update(patient, phoneNumber, email);
            System.out.println("\nPersonal information updated successfully!");
        } else {
            System.out.println("\nNo changes made.");
        }
    }
}
