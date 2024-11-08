package service;

import java.util.Map;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import enums.UserRole;
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

    public User findOne(String id, boolean findNonStaff, boolean findStaff) {
        if(findStaff){
            Administrator admin = administratorRepository.findOne(id);
            if (admin != null) return admin;
    
            Doctor doctor = doctorRepository.findOne(id);
            if (doctor != null) return doctor;
    
            Pharmacist pharmacist = pharmacistRepository.findOne(id);
            if (pharmacist != null) return pharmacist;
        }

        if(findNonStaff){
            Patient patient = patientRepository.findOne(id);
            if (patient != null) return patient;
        }

        return null;
    }

    public void updateUser(User user){
        UserRole role = this.determineRole(user);

        switch(role){
            case PATIENT:
                patientRepository.update((Patient) user);
                break;
            case DOCTOR:
                doctorRepository.update((Doctor) user);
                break;
            case PHARMACIST:
                pharmacistRepository.update((Pharmacist) user);
                break;  
            case ADMINISTRATOR:
                administratorRepository.update((Administrator) user);
                break;
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
        return;
    }

    public void removeUser(User user){
        UserRole role = this.determineRole(user);

        switch(role){
            case PATIENT:
                patientRepository.delete((Patient) user);
                break;
            case DOCTOR:
                doctorRepository.delete((Doctor) user);
                break;
            case PHARMACIST:
                pharmacistRepository.delete((Pharmacist) user);
                break;  
            case ADMINISTRATOR:
                administratorRepository.delete((Administrator) user);
                break;
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
        return;
    }



    public UserRole determineRole(User user) {
        return Optional.ofNullable(user)
            .map(User::getClass)
            .map(ROLE_MAPPINGS::get)
            .orElseThrow(() -> new IllegalArgumentException("Unknown user type: " + user.getClass()));
    }

    private static final Map<Class<? extends User>, UserRole> ROLE_MAPPINGS = 
        Map.of(
            Patient.class, UserRole.PATIENT,
            Doctor.class, UserRole.DOCTOR,
            Pharmacist.class, UserRole.PHARMACIST,
            Administrator.class, UserRole.ADMINISTRATOR
    );
}
