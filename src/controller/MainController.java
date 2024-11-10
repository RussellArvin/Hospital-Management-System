package controller;

import java.util.Scanner;

import model.Administrator;
import model.Doctor;
import model.Patient;
import model.Pharmacist;
import model.User;
import repository.AdministratorRepository;
import repository.AppointmentOutcomeRepository;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.MedicineRepository;
import repository.PatientRepository;
import repository.PharmacistRepository;
import repository.PrescriptionRepository;
import repository.ReplenishmentRequestRepository;
import service.AppointmentOutcomeService;
import service.AppointmentScheduleService;
import service.AppointmentService;
import service.AuthService;
import service.DoctorService;
import service.InventoryService;
import service.MedicalRecordService;
import service.PatientService;
import service.ReplenishmentRequestService;
import service.StaffService;
import service.UserService;
import ui.LoginMenuUI;
import enums.UserRole;

public class MainController extends BaseController<LoginMenuUI> {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private PharmacistRepository pharmacistRepository;
    private AdministratorRepository administratorRepository;
    private MedicineRepository medicineRepository;
    private ReplenishmentRequestRepository replenishmentRequestRepository;
    private AppointmentRepository appointmentRepository;
    private AppointmentOutcomeRepository appointmentOutcomeRepository;
    private PrescriptionRepository prescriptionRepository;

    private AuthService authService;
    private UserService userService;
    private MedicalRecordService medicalRecordService;
    private InventoryService inventoryService;
    private ReplenishmentRequestService replenishmentRequestService;
    private StaffService staffService;
    private AppointmentService appointmentService;
    private AppointmentScheduleService appointmentScheduleService;
    private AppointmentOutcomeService appointmentOutcomeService;
    private DoctorService doctorService;
    private PatientService patientService;


    public MainController(Scanner scanner) {
        super(new LoginMenuUI(),scanner);  // Fixed constructor parameter order to match BaseController

        this.patientRepository = new PatientRepository();
        this.doctorRepository = new DoctorRepository();
        this.pharmacistRepository = new PharmacistRepository();
        this.administratorRepository = new AdministratorRepository();
        this.medicineRepository = new MedicineRepository();
        this.replenishmentRequestRepository = new ReplenishmentRequestRepository();
        this.appointmentRepository = new AppointmentRepository();
        this.appointmentOutcomeRepository = new AppointmentOutcomeRepository();
        this.prescriptionRepository = new PrescriptionRepository();

        this.authService = new AuthService(
            this.patientRepository,
            this.doctorRepository, 
            this.pharmacistRepository,
            this.administratorRepository
        );

        this.inventoryService = new InventoryService(medicineRepository);

        this.medicalRecordService = new MedicalRecordService(patientRepository);
        this.replenishmentRequestService = new ReplenishmentRequestService(replenishmentRequestRepository, medicineRepository, pharmacistRepository);
        this.userService = new UserService(administratorRepository, pharmacistRepository, doctorRepository, patientRepository);
        this.staffService = new StaffService(doctorRepository, pharmacistRepository, administratorRepository, patientRepository,this.userService);
        this.appointmentService = new AppointmentService(appointmentRepository, doctorRepository, patientRepository);
        this.appointmentScheduleService = new AppointmentScheduleService(appointmentService, patientRepository, doctorRepository);
        this.appointmentOutcomeService = new AppointmentOutcomeService(appointmentOutcomeRepository, prescriptionRepository, appointmentRepository, doctorRepository, patientRepository, medicineRepository);
        this.doctorService = new DoctorService(doctorRepository,appointmentRepository,patientRepository);
        this.patientService = new PatientService(patientRepository, appointmentRepository, doctorRepository);
    }

    public void handleUserInput() {
        while(true) {
            menu.printOptions();
            String choice = scanner.nextLine();

            if(choice.equals("1")){
                User user = handlePatientLogin();
                if(user != null){
                    handleUserRole(user);
                }
                return;
            }
            else if(choice.equals("2")){
                User user = handleStaffLogin();
                if(user != null){
                    handleUserRole(user);
                }
                return;
            }
            else if(choice.equals("3")){
                return;
            } else {
                System.out.println("Invalid option!");;
            }
        }
    }

    private User handlePatientLogin() {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = authService.Login(id, password,false);

        if(user == null) {
            System.out.println("\nLogin failed. Invalid ID or password.");
            return null;
        } else {
            System.out.println("\nLogin Successful");
            System.out.println("Welcome, " + user.getName());
            return user;
        }
    }

    private User handleStaffLogin() {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = authService.Login(id, password,true);

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
        UserRole role = userService.determineRole(user);

        switch(role){
            case PATIENT:
                PatientController patientController = new PatientController(
                    this.scanner,
                    (Patient) user,
                    this.medicalRecordService,
                    this.appointmentService,
                    this.appointmentScheduleService,
                    this.appointmentOutcomeService,
                    this.inventoryService
                );
                patientController.handleUserInput();
                break;
            case DOCTOR:
                DoctorController doctorController = new DoctorController(
                    this.scanner,
                    (Doctor) user,
                    this.appointmentService,
                    this.appointmentOutcomeService,
                    appointmentScheduleService,
                    patientService,
                    inventoryService,
                    doctorService
                    
                );
                doctorController.handleUserInput();
                break;
            case PHARMACIST:
                PharmacistController pharmacistController = new PharmacistController(
                    this.scanner,
                    (Pharmacist) user,
                    this.inventoryService,
                    this.replenishmentRequestService,
                    this.appointmentOutcomeService
                );
                pharmacistController.handleUserInput();
                break;
            case ADMINISTRATOR:
                AdministratorController administratorController = new AdministratorController(
                    this.scanner,
                    (Administrator) user,
                    replenishmentRequestService,
                    staffService,
                    inventoryService
                );
                administratorController.handleUserInput();
                break;
            default:
                System.out.println("Invalid role!");
                return;
        }
    }
}