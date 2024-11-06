package service;

import model.Patient;
import model.User;
import repository.PatientRepository;

public class AuthService {
    private PatientRepository patientRepository;

    public AuthService(
        PatientRepository patientRepository
    ) {
        this.patientRepository = patientRepository;
    }

    public User Login(String id, String password) {
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

        return null;
    }
}
