package controller;

import enums.AppointmentAction;
import enums.UserRole;
import java.util.Scanner;
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
import ui.PatientMenuUI;
import ui.PatientRecordUI;
import validator.InputValidator;

/**
 * Controller for handling patient-related actions and interactions.
 * Provides functionalities for viewing medical records, updating personal information,
 * managing appointments, and viewing appointment outcomes.
 * 
 * @author Russell Arvin 
 * @version 1.0 
 */
public class PatientController extends BaseController<PatientMenuUI> {
    private Patient patient;  // Patient associated with this controller

    private MedicalRecordService medicalRecordService;  // Service for handling medical records
    private AppointmentService appointmentService;  // Service for managing appointments
    private AppointmentScheduleService appointmentScheduleService;  // Service for scheduling appointments
    private AppointmentTableUI appointmentTableUI;  // UI for displaying appointment information
    private AppointmentOutcomeTableUI appointmentOutcomeUI;  // UI for displaying appointment outcomes
    private AppointmentScheduleUI appointmentScheduleUI;  // UI for managing appointment schedules
    private PatientService patientService;  // Service for handling patient data

    /**
     * Constructs a PatientController with the specified scanner, patient, and various services for handling
     * appointments, medical records, and patient data.
     *
     * @param scanner the Scanner instance for reading user input
     * @param patient the Patient associated with this controller
     * @param medicalRecordService the service for managing medical records
     * @param appointmentService the service for managing appointment data
     * @param appointmentScheduleService the service for scheduling appointments
     * @param appointmentOutcomeService the service for managing appointment outcomes
     * @param inventoryService the service for handling inventory-related data
     * @param patientService the service for handling patient data
     */
    public PatientController(
        Scanner scanner,
        Patient patient,
        MedicalRecordService medicalRecordService,
        AppointmentService appointmentService,
        AppointmentScheduleService appointmentScheduleService,
        AppointmentOutcomeService appointmentOutcomeService,
        InventoryService inventoryService,
        PatientService patientService
    ) {
        super(new PatientMenuUI(), scanner);
        this.patient = patient;
        this.medicalRecordService = medicalRecordService;
        this.appointmentService = appointmentService;
        this.appointmentScheduleService = appointmentScheduleService;
        this.patientService = patientService;
        this.appointmentTableUI = new AppointmentTableUI(appointmentService, appointmentScheduleService, appointmentOutcomeService, inventoryService, patient, UserRole.PATIENT);
        this.appointmentOutcomeUI = new AppointmentOutcomeTableUI(appointmentOutcomeService, patient);
        this.appointmentScheduleUI = new AppointmentScheduleUI(scanner, appointmentService, UserRole.PATIENT, null);
    }

    /**
     * Main loop for handling patient user input, providing options to view medical records,
     * update personal information, manage appointments, and view appointment outcomes.
     */
    public void handleUserInput() {
        while (true) {
            menu.printOptions();
            String choice = scanner.nextLine();
    
            if (choice.equals("1")) {
                viewMedialRecord();
            } else if (choice.equals("2")) {
                updatePersonalInformation();
            } else if (choice.equals("3")) {
                appointmentScheduleUI.display();
            } else if (choice.equals("4")) {
                scheduleAppointment();
            } else if (choice.equals("5")) {
                appointmentTableUI.display(scanner, AppointmentAction.RESCHEDULE);
            } else if (choice.equals("6")) {
                appointmentTableUI.display(scanner, AppointmentAction.CANCEL);
            } else if (choice.equals("7")) {
                appointmentTableUI.display(scanner, AppointmentAction.VIEW);
            } else if (choice.equals("8")) {
                appointmentOutcomeUI.display(scanner, false);
            } else if (choice.equals("9")) {
                super.handleLogout(this.patient);
                return;
            } else {
               super.invalidOption();
            }
        }
    }

    /**
     * Displays the patient's medical record details and the latest patient vitals.
     */
    private void viewMedialRecord() {
        MedicalRecordDetail[] records = patientService.getMedicalRecordsByPatientId(patient.getId());
        PatientVital vital = patientService.geLatestPatientVitalByPatientId(patient.getId());
        PatientRecordUI.display(this.patient, this.scanner, records, vital);
    }

    /**
     * Allows the patient to update personal information, such as phone number and email.
     * Validates the inputs before updating.
     */
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

    /**
     * Displays the UI for scheduling a new appointment for the patient.
     */
    public void scheduleAppointment() {
        CreateAppointmentUI.display(scanner, appointmentService, appointmentScheduleService, patient);
    }
}
