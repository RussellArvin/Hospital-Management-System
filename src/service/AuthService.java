package service;

import model.Administrator;
import model.Doctor;
import model.Patient;
import model.Pharmacist;
import model.User;
import repository.AdministratorRepository;
import repository.DoctorRepository;
import repository.PatientRepository;
import repository.PharmacistRepository;

public class AuthService {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private PharmacistRepository pharmacistRepository;
    private AdministratorRepository administratorRepository;
    private UserService userService;

    public AuthService(
        PatientRepository patientRepository,
        DoctorRepository doctorRepository,
        PharmacistRepository pharmacistRepository,
        AdministratorRepository administratorRepository
    ) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.administratorRepository = administratorRepository;
        this.userService = new UserService(administratorRepository, pharmacistRepository, doctorRepository, patientRepository);
    }

    public User Login(String id, String password) {
        System.out.println("Login");
        if (id == null || password == null) {
            return null;
        }

        User user = userService.findOne(id);
        if(user != null && user.validatePassword(password)){
            return user;
        }
        else if(user!= null && !user.validatePassword(password)){
            return null;
        }

        return null;
    }
}
