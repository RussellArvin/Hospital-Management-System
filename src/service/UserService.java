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

public class UserService {
    private AdministratorRepository administratorRepository;
    private PharmacistRepository pharmacistRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;

    public UserService(
        AdministratorRepository administratorRepository,
        PharmacistRepository pharmacistRepository,
        DoctorRepository doctorRepository,
        PatientRepository patientRepository
    ) {
        this.administratorRepository = administratorRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public User findOne(String id) {
        Administrator admin = administratorRepository.findOne(id);
        if (admin != null) return admin;

        Doctor doctor = doctorRepository.findOne(id);
        if (doctor != null) return doctor;

        Pharmacist pharmacist = pharmacistRepository.findOne(id);
        if (pharmacist != null) return pharmacist;

        Patient patient = patientRepository.findOne(id);
        if (patient != null) return patient;

        return null;
    }
}
