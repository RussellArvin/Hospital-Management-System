package controller;

import java.util.Scanner;

import enums.AppointmentAction;
import enums.UserRole;
import model.Nurse;
import model.Patient;
import service.AppointmentService;
import service.PatientService;
import ui.AppointmentTableUI;
import ui.CreatePatientVitalUI;
import ui.NurseMenuUI;
import ui.PatientTableUI;

public class NurseController extends BaseController<NurseMenuUI> {
    private Nurse nurse;
    private PatientService patientService;
    private AppointmentTableUI appointmentTableUI;
    private CreatePatientVitalUI createPatientVitalUI;

    public NurseController(
        Scanner scanner,
        Nurse nurse,
        PatientService patientService,
        AppointmentService appointmentService
    ){
        super(new NurseMenuUI(), scanner);
        this.nurse = nurse;
        this.patientService = patientService;
        this.appointmentTableUI = new AppointmentTableUI(appointmentService, null, null, null, nurse, UserRole.NURSE);
        this.createPatientVitalUI = new CreatePatientVitalUI(scanner, patientService);
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();

            if(choice.equals("1")){
                viewPatients();
            }
            else if(choice.equals("2")){
                    viewTodaysAppointments();
            }
            else if(choice.equals("3")){
                createPatientVitalUI.display();
            }
            else if(choice.equals("4")){
                super.handleLogout(nurse);
                break;
            } else {
                super.invalidOption();
            }
        }
    }

    private void viewPatients(){
        Patient[] patients = patientService.findAll();
        PatientTableUI patientTableUI = new PatientTableUI(scanner, patientService, null, patients);
        patientTableUI.display(false);
    }

    private void viewTodaysAppointments(){
        appointmentTableUI.display(scanner, AppointmentAction.VIEW);
    }
}
