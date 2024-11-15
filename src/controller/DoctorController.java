package controller;

import enums.AppointmentAction;
import enums.UserRole;
import java.util.Scanner;
import model.Doctor;
import model.Patient;
import service.AppointmentOutcomeService;
import service.AppointmentScheduleService;
import service.AppointmentService;
import service.DoctorService;
import service.InventoryService;
import service.PatientService;
import ui.AppointmentScheduleUI;
import ui.AppointmentTableUI;
import ui.DoctorAvailabilityUI;
import ui.DoctorMenuUI;
import ui.PatientTableUI;

/**
 * Controller class responsible for handling user input and managing actions specific to the Doctor role.
 * Extends BaseController and interacts with various services and UI components.
 * 
 * @author Lim Jun Howe
 * @version 1.0
 */
public class DoctorController extends BaseController<DoctorMenuUI> {
    private Doctor doctor;  // The doctor associated with this controller.
    private AppointmentTableUI tableUI;  // UI for managing appointment tables.
    private DoctorService doctorService;  // Service for managing doctor-specific data.
    private PatientService patientService;  // Service for managing patient-specific data.
    private AppointmentScheduleUI appointmentScheduleUI;  // UI for managing doctor's appointment schedule.

    /**
     * Constructs a DoctorController with the specified parameters and initializes UI components and services.
     * 
     * @param scanner The scanner used for reading user input.
     * @param doctor The doctor associated with the controller.
     * @param appointmentService Service for managing appointments.
     * @param appointmentOutcomeService Service for managing appointment outcomes.
     * @param appointmentScheduleService Service for managing appointment schedules.
     * @param patientService Service for managing patient data.
     * @param inventoryService Service for managing inventory.
     * @param doctorService Service for managing doctor data.
     */
    public DoctorController(
        Scanner scanner,
        Doctor doctor,
        AppointmentService appointmentService,
        AppointmentOutcomeService appointmentOutcomeService,
        AppointmentScheduleService appointmentScheduleService,
        PatientService patientService,
        InventoryService inventoryService,
        DoctorService doctorService
    ) {
        super(new DoctorMenuUI(), scanner);
        this.doctor = doctor;
        this.tableUI = new AppointmentTableUI(appointmentService, appointmentScheduleService, appointmentOutcomeService, inventoryService, doctor, UserRole.DOCTOR);
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentScheduleUI = new AppointmentScheduleUI(scanner, appointmentService, UserRole.DOCTOR, doctor);
    }

    /**
     * Handles user input by presenting options and triggering the corresponding actions.
     */
    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();
            if(choice.equals("1")){
                viewPatientRecords();
            }
            else if(choice.equals("2")){
                updatePatientRecords();
            }
            else if(choice.equals("3")){
                viewPersonalSchedule();
            }
            else if(choice.equals("4")){
                setAvailability();
            }
            else if(choice.equals("5")){
                acceptDeclineAppointments();
            }
            else if(choice.equals("6")){
                viewUpcomingAppointments();
            }
            else if(choice.equals("7")){
                recordOutcome();
            }
            else if(choice.equals("8")){
                super.handleLogout(doctor);
                break;
            }
        }
    }

    /**
     * Updates patient records by displaying a table of the doctor's patients for editing.
     */
    private void updatePatientRecords(){
        Patient[] patients = doctorService.getDoctorPatients(doctor.getId());
        PatientTableUI patientTableUI = new PatientTableUI(scanner, patientService, doctor.getId(), patients);
        patientTableUI.display(true);
    }

    /**
     * Views the patient records without editing, displaying a table of the doctor's patients.
     */
    private void viewPatientRecords(){
        Patient[] patients = doctorService.getDoctorPatients(doctor.getId());
        PatientTableUI patientTableUI = new PatientTableUI(scanner, patientService, doctor.getId(), patients);
        patientTableUI.display(false);
    }

    /**
     * Views the doctor's personal schedule, displaying scheduled appointments.
     */
    private void viewPersonalSchedule(){
        appointmentScheduleUI.display();
    }

    /**
     * Allows the doctor to accept or decline appointments.
     */
    private void acceptDeclineAppointments(){
        tableUI.display(scanner, AppointmentAction.APPROVE);
    }

    /**
     * Views upcoming appointments for the doctor.
     */
    private void viewUpcomingAppointments(){
        tableUI.display(scanner, AppointmentAction.VIEW);
    }

    /**
     * Records the outcome of an appointment.
     */
    private void recordOutcome(){
        tableUI.display(scanner, AppointmentAction.OUTCOME);
    }

    /**
     * Sets the doctor's availability for appointments.
     */
    private void setAvailability(){
        DoctorAvailabilityUI.display(scanner, doctorService, doctor);
    }
}
