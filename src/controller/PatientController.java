package controller;

import java.util.Scanner;

import enums.UserRole;
import enums.AppointmentAction;
import model.MedicalRecordDetail;
import model.Patient;
import model.PatientVital;
import service.AppointmentOutcomeService;
import service.AppointmentScheduleService;
import service.AppointmentService;
import service.InventoryService;
import service.MedicalRecordService;
import service.PatientService;
import ui.AppointmentOutcomeTableUI;
import ui.AppointmentScheduleUI;
import ui.AppointmentTableUI;
import ui.CreateAppointmentUI;
import ui.PatientRecordUI;
import ui.PatientMenuUI;
import validator.InputValidator;

public class PatientController extends BaseController<PatientMenuUI> {
    private Patient patient;

    private MedicalRecordService medicalRecordService;
    private AppointmentService appointmentService;
    private AppointmentScheduleService appointmentScheduleService;
    private AppointmentTableUI appointmentTableUI;
    private AppointmentOutcomeTableUI appointmentOutcomeUI;
    private AppointmentScheduleUI appointmentScheduleUI;
    private PatientService patientService;
    
    public PatientController(
        Scanner scanner,
        Patient patient,
        MedicalRecordService medicalRecordService,
        AppointmentService appointmentService,
        AppointmentScheduleService appointmentScheduleService,
        AppointmentOutcomeService appointmentOutcomeService,
        InventoryService inventoryService,
        PatientService patientService
    ){
        super(new PatientMenuUI(),scanner);

        this.patient = patient;
        this.medicalRecordService = medicalRecordService;
        this.appointmentService = appointmentService;
        this.appointmentScheduleService = appointmentScheduleService;
        this.patientService = patientService;
        this.appointmentTableUI = new AppointmentTableUI(appointmentService, appointmentScheduleService, appointmentOutcomeService, inventoryService, patient, UserRole.PATIENT);
        this.appointmentOutcomeUI = new AppointmentOutcomeTableUI(appointmentOutcomeService, patient);
        this.appointmentScheduleUI = new AppointmentScheduleUI(scanner, appointmentService, UserRole.PATIENT, null);
    }

    public void handleUserInput() {
        while(true) {
            menu.printOptions();
            String choice = scanner.nextLine();
    
            if(choice.equals("1")) {
                viewMedialRecord();
            }
            else if(choice.equals("2")){
                updatePersonalInformation();
            }
            else if(choice.equals("3")){
                appointmentScheduleUI.display();
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
            else if(choice.equals("8")){
                appointmentOutcomeUI.display(scanner,false);
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

    private void viewMedialRecord(){
        MedicalRecordDetail[] records = patientService.getMedicalRecordsByPatientId(patient.getId());
        PatientVital vital = patientService.geLatestPatientVitalByPatientId(patient.getId());

        PatientRecordUI.display(this.patient, this.scanner, records,vital);
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
