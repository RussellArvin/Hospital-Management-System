package controller;

import java.util.Scanner;

import model.Doctor;
import model.Patient;
import model.User;
import repository.DoctorRepository;
import repository.PatientRepository;
import service.AuthService;
import ui.LoginMenuUI;

public class AuthController extends BaseController<LoginMenuUI> {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;

    private AuthService authService;

    public AuthController(Scanner scanner) {
        super(new LoginMenuUI(),scanner);  // Fixed constructor parameter order to match BaseController

        this.patientRepository = new PatientRepository();
        this.doctorRepository = new DoctorRepository();

        this.authService = new AuthService(this.patientRepository,this.doctorRepository);
    }

    public void handleUserInput() {
        while(true) {
            menu.printOptions();
            String choice = scanner.nextLine();

            if(choice.equals("1")){
                User user = handleLogin();
                if(user != null){
                    handleUserRole(user);
                }
                return;
            }
            else if(choice.equals("2")){
                return;
            } else {
                System.out.println("Invalid option!");;
            }
        }
    }

    private User handleLogin() {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = authService.Login(id, password);

        if(user == null) {
            System.out.println("\nLogin failed. Invalid ID or password.");
            return null;
        } else {
            System.out.println("\nLogin Successful");
            System.out.println("Welcome, " + user.getName());
            return user;
        }
    }

    private void handleUserRole(User user) {
        if(user instanceof Patient patient) { 
            PatientController patientController = new PatientController(
                this.scanner,
                patient,
                this.patientRepository
            );
            patientController.handleUserInput();
        }

        if(user instanceof Doctor doctor){
            DoctorController doctorController = new DoctorController(
                this.scanner,
                doctor,
                this.doctorRepository
            );
            doctorController.handleUserInput();
        }
    }
}