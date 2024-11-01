package service;

import repository.PatientRepository;
import model.Patient;
import model.User;

public class UserService {
    private final PatientRepository patientRepository;

    public UserService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public User Login(String id, String password){
        Patient patient = patientRepository.findOne(id);

        if(patient.validatePassword) return patient;
        else return null;
    }
}