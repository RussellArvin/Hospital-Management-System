package controller;

import java.time.LocalDate;
import java.util.Scanner;

import enums.UserRole;
import model.AppointmentDetail;
import model.Doctor;
import service.AppointmentService;
import ui.AppointmentScheduleUI;
import ui.DoctorMenuUI;

public class DoctorController extends BaseController<DoctorMenuUI> {
    private Doctor doctor;
    private AppointmentService appointmentService;

    public DoctorController(
        Scanner scanner,
        Doctor doctor,
        AppointmentService appointmentService
    ) {
        super(new DoctorMenuUI(), scanner);
        this.doctor = doctor;
        this.appointmentService = appointmentService;
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();
            if(choice.equals("3")){
                viewPersonalSchedule();
            }
            else if(choice.equals("8")){
                super.handleLogout(doctor);
                break;
            }
        }
    }

    public void viewPersonalSchedule(){
        AppointmentScheduleUI.display(scanner, appointmentService, UserRole.DOCTOR, doctor);
    }
}
