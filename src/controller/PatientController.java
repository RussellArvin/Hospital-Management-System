package controller;

import java.util.Scanner;

import enums.UserRole;
import enums.AppointmentAction;
import model.AppointmentDetail;
import model.Patient;
import service.AppointmentScheduleService;
import service.AppointmentService;
import service.MedicalRecordService;
import ui.AppointmentScheduleUI;
import ui.AppointmentTableUI;
import ui.CreateAppointmentUI;
import ui.MedicalRecordUI;
import ui.PatientMenuUI;
import validator.InputValidator;

public class PatientController extends BaseController<PatientMenuUI> {
    private Patient patient;

    private MedicalRecordService medicalRecordService;
    private AppointmentService appointmentService;
    private AppointmentScheduleService appointmentScheduleService;
    private AppointmentTableUI appointmentTableUI;
    
    public PatientController(
        Scanner scanner,
        Patient patient,
        MedicalRecordService medicalRecordService,
        AppointmentService appointmentService,
        AppointmentScheduleService appointmentScheduleService
    ){
        super(new PatientMenuUI(),scanner);

        this.patient = patient;
        this.medicalRecordService = medicalRecordService;
        this.appointmentService = appointmentService;
        this.appointmentScheduleService = appointmentScheduleService;
        this.appointmentTableUI = new AppointmentTableUI(appointmentService, appointmentScheduleService, patient, UserRole.PATIENT);
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
            else if(choice.equals("3")){
                AppointmentScheduleUI.display(scanner,appointmentService,UserRole.PATIENT,null);
            }
            else if(choice.equals("4")){
                scheduleAppointment();
            }
            else if(choice.equals("5")){
                appointmentTableUI.display(scanner,AppointmentAction.RESCHEDULE);
            }
            else if(choice.equals("6")){
                appointmentTableUI.display(scanner,AppointmentAction.CANCEL);
            }
            else if(choice.equals("7")){
                appointmentTableUI.display(scanner,AppointmentAction.VIEW);
            }
            else if(choice.equals("9")) {
                super.handleLogout(this.patient);
                return;
            }
            else {
               super.invalidOption();
            }
        }
    }


    private void updatePersonalInformation() {
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

    public void scheduleAppointment(){
        CreateAppointmentUI.display(scanner, appointmentService, appointmentScheduleService, patient);
    }
}
