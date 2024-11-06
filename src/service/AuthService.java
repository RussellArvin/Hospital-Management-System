package service;

import model.Doctor;
import model.Patient;
import model.User;
import repository.DoctorRepository;
import repository.PatientRepository;

public class AuthService {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;

    public AuthService(
        PatientRepository patientRepository,
        DoctorRepository doctorRepository
    ) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
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

        return null;
    }
}
