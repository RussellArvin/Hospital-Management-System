package controller;

import java.util.Scanner;

import enums.AppointmentAction;
import enums.UserRole;
import model.Doctor;
import service.AppointmentOutcomeService;
import service.AppointmentScheduleService;
import service.AppointmentService;
import service.InventoryService;
import ui.AppointmentScheduleUI;
import ui.AppointmentTableUI;
import ui.DoctorMenuUI;

public class DoctorController extends BaseController<DoctorMenuUI> {
    private Doctor doctor;
    private AppointmentService appointmentService;
    private AppointmentTableUI tableUI;

    public DoctorController(
        Scanner scanner,
        Doctor doctor,
        AppointmentService appointmentService,
        AppointmentOutcomeService appointmentOutcomeService,
        AppointmentScheduleService appointmentScheduleService,
        InventoryService inventoryService
    ) {
        super(new DoctorMenuUI(), scanner);
        this.doctor = doctor;
        this.appointmentService = appointmentService;
        this.tableUI = new AppointmentTableUI(appointmentService, appointmentScheduleService, appointmentOutcomeService, inventoryService, doctor, UserRole.DOCTOR);
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();
            if(choice.equals("3")){
                viewPersonalSchedule();
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

    private void viewPersonalSchedule(){
        AppointmentScheduleUI.display(scanner, appointmentService, UserRole.DOCTOR, doctor);
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
}
