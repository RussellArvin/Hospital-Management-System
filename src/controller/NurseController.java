package controller;

import enums.AppointmentAction;
import enums.UserRole;
import java.util.Scanner;
import model.Nurse;
import model.Patient;
import service.AppointmentService;
import service.PatientService;
import ui.AppointmentTableUI;
import ui.CreatePatientVitalUI;
import ui.NurseMenuUI;
import ui.PatientTableUI;

/**
 * Controller for handling nurse-related actions and interactions.
 * Provides functionalities to view patient records, view appointments, and manage patient vitals.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public class NurseController extends BaseController<NurseMenuUI> {
    private Nurse nurse;  // Nurse associated with this controller
    private PatientService patientService;  // Service for handling patient data
    private AppointmentTableUI appointmentTableUI;  // UI for displaying appointment information
    private CreatePatientVitalUI createPatientVitalUI;  // UI for managing patient vitals

    /**
     * Constructs a NurseController with the specified scanner, nurse, patient service, and appointment service.
     *
     * @param scanner the Scanner instance for reading user input
     * @param nurse the Nurse associated with this controller
     * @param patientService the service for managing patient data
     * @param appointmentService the service for managing appointment data
     */
    public NurseController(
        Scanner scanner,
        Nurse nurse,
        PatientService patientService,
        AppointmentService appointmentService
    ) {
        super(new NurseMenuUI(), scanner);
        this.nurse = nurse;
        this.patientService = patientService;
        this.appointmentTableUI = new AppointmentTableUI(appointmentService, null, null, null, nurse, UserRole.NURSE);
        this.createPatientVitalUI = new CreatePatientVitalUI(scanner, patientService);
    }

    /**
     * Main loop for handling nurse user input, providing options to view patients,
     * view today's appointments, create patient vitals, or logout.
     */
    public void handleUserInput() {
        while (true) {
            menu.printOptions();
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                viewPatients();
            } else if (choice.equals("2")) {
                viewTodaysAppointments();
            } else if (choice.equals("3")) {
                createPatientVitalUI.display();
            } else if (choice.equals("4")) {
                super.handleLogout(nurse);
                break;
            } else {
                super.invalidOption();
            }
        }
    }

    /**
     * Displays a list of all patients.
     */
    private void viewPatients() {
        Patient[] patients = patientService.findAll();
        PatientTableUI patientTableUI = new PatientTableUI(scanner, patientService, null, patients);
        patientTableUI.display(false);
    }

    /**
     * Displays today's appointments for the nurse.
     */
    private void viewTodaysAppointments() {
        appointmentTableUI.display(scanner, AppointmentAction.VIEW);
    }
}
