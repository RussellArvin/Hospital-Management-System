package controller;

import java.util.Scanner;

import enums.AppointmentAction;
import enums.UserRole;
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

public class DoctorController extends BaseController<DoctorMenuUI> {
    private Doctor doctor;
    private AppointmentTableUI tableUI;
    private DoctorService doctorService;
    private PatientService patientService;
    private AppointmentScheduleUI appointmentScheduleUI;

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

    private void updatePatientRecords(){
        Patient[] patients = doctorService.getDoctorPatients(doctor.getId());
        PatientTableUI patientTableUI = new PatientTableUI(scanner, patientService, doctor.getId(), patients);
        patientTableUI.display(true);
    }

    private void viewPatientRecords(){
        Patient[] patients = doctorService.getDoctorPatients(doctor.getId());
        PatientTableUI patientTableUI = new PatientTableUI(scanner, patientService, doctor.getId(), patients);
        patientTableUI.display(false);
    }

    private void viewPersonalSchedule(){
        appointmentScheduleUI.display();
    }

    private void acceptDeclineAppointments(){
        tableUI.display(scanner,AppointmentAction.APPROVE);
    }

    private void viewUpcomingAppointments(){
        tableUI.display(scanner, AppointmentAction.VIEW);
    }

    private void recordOutcome(){
        tableUI.display(scanner, AppointmentAction.OUTCOME);
    }

    private void setAvailability(){
        DoctorAvailabilityUI.display(scanner, doctorService, doctor);
    }
}
