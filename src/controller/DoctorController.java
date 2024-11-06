package controller;

import java.util.Scanner;

import model.Doctor;
import repository.DoctorRepository;
import ui.DoctorMenuUI;

public class DoctorController extends BaseController<DoctorMenuUI> {
    private Doctor doctor;
    private DoctorRepository doctorRepository;

    public DoctorController(
        Scanner scanner,
        Doctor doctor,
        DoctorRepository doctorRepository
    ) {
        super(new DoctorMenuUI(), scanner);
        this.doctor = doctor;
        this.doctorRepository = doctorRepository;
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();
        }
    }
}
