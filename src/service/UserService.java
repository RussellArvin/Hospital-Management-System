package service;

import enums.UserRole;
import java.util.Map;
import java.util.Optional;
import model.Administrator;
import model.Doctor;
import model.Nurse;
import model.Patient;
import model.Pharmacist;
import model.User;
import repository.DoctorRepository;
import repository.NurseRepository;
import repository.PatientRepository;
import repository.PharmacistRepository;
import repository.base.CsvRepository;
import repository.mapper.AdministratorMapper;

/**
 * The UserService class provides methods for managing users, including finding, updating, 
 * and removing users across different user roles (patients, doctors, pharmacists, administrators, and nurses).
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class UserService {
    private CsvRepository<Administrator,AdministratorMapper> administratorRepository;
    private PharmacistRepository pharmacistRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private NurseRepository nurseRepository;

    /**
     * Constructs a UserService with the required repositories.
     *
     * @param administratorRepository Repository for managing administrators.
     * @param pharmacistRepository    Repository for managing pharmacists.
     * @param doctorRepository        Repository for managing doctors.
     * @param patientRepository       Repository for managing patients.
     * @param nurseRepository         Repository for managing nurses.
     */
    public UserService(
        CsvRepository<Administrator,AdministratorMapper> administratorRepository,
        PharmacistRepository pharmacistRepository,
        DoctorRepository doctorRepository,
        PatientRepository patientRepository,
        NurseRepository nurseRepository
    ) {
        this.administratorRepository = administratorRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.nurseRepository = nurseRepository;
    }

    /**
     * Finds a user by their ID.
     *
     * @param id           The ID of the user to find.
     * @param findNonStaff A flag indicating whether to search for non-staff users (patients).
     * @param findStaff    A flag indicating whether to search for staff users.
     * @return The user if found, or null otherwise.
     */
    public User findOne(String id, boolean findNonStaff, boolean findStaff) {
        if (findStaff) {
            Administrator admin = administratorRepository.findOne(id);
            if (admin != null) return admin;
    
            Doctor doctor = doctorRepository.findOne(id);
            if (doctor != null) return doctor;
    
            Pharmacist pharmacist = pharmacistRepository.findOne(id);
            if (pharmacist != null) return pharmacist;

            Nurse nurse = nurseRepository.findOne(id);
            if (nurse != null) return nurse;
        }

        if (findNonStaff) {
            Patient patient = patientRepository.findOne(id);
            if (patient != null) return patient;
        }

        return null;
    }

    /**
     * Updates a user's information in the appropriate repository based on their role.
     *
     * @param user The user to update.
     */
    public void updateUser(User user) {
        UserRole role = this.determineRole(user);

        switch (role) {
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
            case NURSE:
                nurseRepository.update((Nurse) user);
                break;
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
        return;
    }

    /**
     * Removes a user from the appropriate repository based on their role.
     *
     * @param user The user to remove.
     */
    public void removeUser(User user) {
        UserRole role = this.determineRole(user);

        switch (role) {
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
            case NURSE:
                nurseRepository.delete((Nurse) user);
                break;
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
        return;
    }

    /**
     * Determines the role of a given user based on their class type.
     *
     * @param user The user whose role is to be determined.
     * @return The UserRole of the user.
     * @throws IllegalArgumentException If the user type is unknown.
     */
    public UserRole determineRole(User user) {
        return Optional.ofNullable(user)
            .map(User::getClass)
            .map(ROLE_MAPPINGS::get)
            .orElseThrow(() -> new IllegalArgumentException("Unknown user type: " + user.getClass()));
    }

    /**
     * A mapping of user classes to their corresponding UserRole enums.
     */
    private static final Map<Class<? extends User>, UserRole> ROLE_MAPPINGS = 
        Map.of(
            Patient.class, UserRole.PATIENT,
            Doctor.class, UserRole.DOCTOR,
            Pharmacist.class, UserRole.PHARMACIST,
            Administrator.class, UserRole.ADMINISTRATOR,
            Nurse.class, UserRole.NURSE
    );
}
