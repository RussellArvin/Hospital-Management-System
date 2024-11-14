package controller;

import java.util.Scanner;

import model.Administrator;
import model.Doctor;
import model.Patient;
import model.Pharmacist;
import model.User;
import model.Nurse;
import repository.AdministratorRepository;
import repository.AppointmentOutcomeRepository;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.MedicalRecordRepository;
import repository.MedicineRepository;
import repository.NurseRepository;
import repository.PatientRepository;
import repository.PatientVitalRepository;
import repository.PharmacistRepository;
import repository.PrescriptionRepository;
import repository.ReplenishmentRequestRepository;
import service.AppointmentOutcomeService;
import service.AppointmentScheduleService;
import service.AppointmentService;
import service.AuthService;
import service.DoctorService;
import service.InitialisationService;
import service.InventoryService;
import service.MedicalRecordService;
import service.PatientService;
import service.ReplenishmentRequestService;
import service.StaffService;
import service.UserService;
import ui.LoginMenuUI;
import util.Constant;
import enums.UserRole;

public class MainController extends BaseController<LoginMenuUI> {
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
    private InitialisationService initialisationService;


    public MainController(Scanner scanner) {
        super(new LoginMenuUI(),scanner);

        PatientRepository patientRepository = new PatientRepository();
        DoctorRepository doctorRepository = new DoctorRepository();
        PharmacistRepository pharmacistRepository = new PharmacistRepository();
        AdministratorRepository administratorRepository = new AdministratorRepository();
        MedicineRepository medicineRepository = new MedicineRepository();
        ReplenishmentRequestRepository replenishmentRequestRepository = new ReplenishmentRequestRepository();
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        AppointmentOutcomeRepository appointmentOutcomeRepository = new AppointmentOutcomeRepository();
        PrescriptionRepository prescriptionRepository = new PrescriptionRepository();
        MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository();
        NurseRepository nurseRepository = new NurseRepository();
        PatientVitalRepository patientVitalRepository = new PatientVitalRepository();

        this.userService = new UserService(administratorRepository, pharmacistRepository, doctorRepository, patientRepository, nurseRepository);
        this.authService = new AuthService(userService);
        this.inventoryService = new InventoryService(medicineRepository);
        this.medicalRecordService = new MedicalRecordService(patientRepository);
        this.replenishmentRequestService = new ReplenishmentRequestService(replenishmentRequestRepository, medicineRepository, pharmacistRepository);
        this.staffService = new StaffService(doctorRepository, pharmacistRepository, administratorRepository, patientRepository,nurseRepository,this.userService);
        this.appointmentService = new AppointmentService(appointmentRepository, doctorRepository, patientRepository);
        this.appointmentScheduleService = new AppointmentScheduleService(appointmentService, patientRepository, doctorRepository);
        this.appointmentOutcomeService = new AppointmentOutcomeService(appointmentOutcomeRepository, prescriptionRepository, appointmentRepository, doctorRepository, patientRepository, medicineRepository);
        this.doctorService = new DoctorService(doctorRepository,appointmentRepository,patientRepository);
        this.patientService = new PatientService(patientRepository, appointmentRepository, doctorRepository, medicalRecordRepository, patientVitalRepository);
        this.initialisationService = new InitialisationService(administratorRepository, patientRepository, pharmacistRepository, doctorRepository, nurseRepository, medicineRepository);
    }

    public void handleUserInput() {
        initialisationService.initialise();
        while(true) {
            menu.printOptions();
            String choice = scanner.nextLine();

            if(choice.equals("1")){
                handleLogin(false);
 
            }
            else if(choice.equals("2")){
                handleLogin(true);
            }
            else if(choice.equals("3")){
                return;
            } else {
                System.out.println("Invalid option!");;
            }
        }
    }

    private void handleLogin(boolean isStaff){
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = authService.Login(id, password, isStaff);

        if(user == null) {
            System.out.println("\nLogin failed. Invalid ID or password.");
            return;
        } else {
            System.out.println("\nLogin Successful");
            System.out.println("Welcome, " + user.getName());
            if(password.equals(Constant.DEFAULT_PASSWORD)) {
                System.out.println("Please enter an updated password: ");
                String updatedPassword = scanner.nextLine();
                String error = authService.setPassword(user, updatedPassword);
                if(error != null) {
                    System.out.println(error);
                    return;
                }
            }
            handleUserRole(user);
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
                    this.inventoryService,
                    this.patientService
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
                    inventoryService,
                    appointmentService,
                    appointmentScheduleService,
                    appointmentOutcomeService
                );
                administratorController.handleUserInput();
                break;
            case NURSE:
                NurseController nurseController =  new NurseController(scanner, (Nurse) user, patientService, appointmentService);
                nurseController.handleUserInput();
                break;
            default:
                System.out.println("Invalid role!");
                return;
        }
    }
}