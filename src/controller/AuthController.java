package controller;

import java.util.Scanner;

import model.Administrator;
import model.Doctor;
import model.Patient;
import model.Pharmacist;
import model.User;
import repository.AdministratorRepository;
import repository.DoctorRepository;
import repository.MedicineRepository;
import repository.PatientRepository;
import repository.PharmacistRepository;
import service.AuthService;
import ui.LoginMenuUI;

public class AuthController extends BaseController<LoginMenuUI> {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private PharmacistRepository pharmacistRepository;
    private AdministratorRepository administratorRepository;
    private MedicineRepository medicineRepository;

    private AuthService authService;

    public AuthController(Scanner scanner) {
        super(new LoginMenuUI(),scanner);  // Fixed constructor parameter order to match BaseController

        this.patientRepository = new PatientRepository();
        this.doctorRepository = new DoctorRepository();
        this.pharmacistRepository = new PharmacistRepository();
        this.administratorRepository = new AdministratorRepository();
        this.medicineRepository = new MedicineRepository();

        this.authService = new AuthService(
            this.patientRepository,
            this.doctorRepository, 
            this.pharmacistRepository,
            this.administratorRepository
        );
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

        if(user instanceof Pharmacist pharmacist){
            PharmacistController pharmacistController = new PharmacistController(
                this.scanner,
                pharmacist,
                this.pharmacistRepository,
                this.medicineRepository
            );
            pharmacistController.handleUserInput();
        }

        if(user instanceof Administrator admin){
            AdministratorController administratorController = new AdministratorController(
                this.scanner,
                admin,
                this.administratorRepository,
                this.doctorRepository,
                this.pharmacistRepository
            );
            administratorController.handleUserInput();
        }
    }
}