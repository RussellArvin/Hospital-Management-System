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
    }

    public User Login(String id, String password) {
        System.out.println("Login");
        if (id == null || password == null) {
            return null;
        }
        
        //Handle patient
        Patient patient = patientRepository.findOne(id);
        if (patient != null && patient.validatePassword(password)) {
            return patient;
        } else if(patient != null && !patient.validatePassword(password)){
            return null;
        }

        //Handle Doctor
        Doctor doctor = doctorRepository.findOne(id);
        if (doctor != null && doctor.validatePassword(password)) {
            return doctor;
        } else if(doctor != null && !doctor.validatePassword(password)){
            return null;
        }

        //Handle Pharmarcist
        Pharmacist pharmacist = pharmacistRepository.findOne(id);
        if (pharmacist != null && pharmacist.validatePassword(password)) {
            return pharmacist;
        } else if(pharmacist != null && !pharmacist.validatePassword(password)){
            return null;
        }

        //Handle Administrator
        Administrator admin = administratorRepository.findOne(id);
        if (admin != null && admin.validatePassword(password)) {
            return admin;
        } else if(admin != null && !admin.validatePassword(password)){
            return null;
        }

        return null;
    }
}
