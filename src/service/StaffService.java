package service;

import enums.Gender;
import enums.UserRole;
import java.time.LocalDateTime;
import model.Administrator;
import model.Doctor;
import model.Nurse;
import model.Pharmacist;
import model.User;
import repository.AdministratorRepository;
import repository.DoctorRepository;
import repository.NurseRepository;
import repository.PatientRepository;
import repository.PharmacistRepository;
import util.Constant;
import util.PasswordUtil;

/**
 * The StaffService class provides functionality to manage staff data such as doctors, pharmacists,
 * administrators, and nurses. It allows operations like retrieving staff details, adding new staff,
 * removing staff, and updating staff attributes like name, password, and age.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public class StaffService {
    private DoctorRepository doctorRepository;
    private PharmacistRepository pharmacistRepository;
    private AdministratorRepository administratorRepository;
    private NurseRepository nurseRepository;
    private UserService userService;

    /**
     * Constructs a StaffService with the required repositories.
     *
     * @param doctorRepository          Repository for managing doctor data.
     * @param pharmacistRepository      Repository for managing pharmacist data.
     * @param administratorRepository   Repository for managing administrator data.
     * @param patientRepository         Repository for managing patient data (not used here).
     * @param nurseRepository           Repository for managing nurse data.
     * @param userService               Service for managing general user data.
     */
    public StaffService(
        DoctorRepository doctorRepository,
        PharmacistRepository pharmacistRepository,
        AdministratorRepository administratorRepository,
        PatientRepository patientRepository,
        NurseRepository nurseRepository,
        UserService userService
    ) {
        this.doctorRepository = doctorRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.administratorRepository = administratorRepository;
        this.nurseRepository = nurseRepository;
        this.userService = userService;
    }

    /**
     * Retrieves all staff data including doctors, pharmacists, administrators, and nurses.
     *
     * @return An array of User objects representing all staff members.
     */
    public User[] getAllStaffData() {
        Doctor[] doctors = this.doctorRepository.findAll();
        Pharmacist[] pharmacists = this.pharmacistRepository.findAll();
        Administrator[] administrators = this.administratorRepository.findAll();
        Nurse[] nurses = this.nurseRepository.findAll();
    
        User[] staff = new User[doctors.length + pharmacists.length + administrators.length + nurses.length];
    
        System.arraycopy(doctors, 0, staff, 0, doctors.length);
        System.arraycopy(pharmacists, 0, staff, doctors.length, pharmacists.length);
        System.arraycopy(administrators, 0, staff, doctors.length + pharmacists.length, administrators.length);
        System.arraycopy(nurses, 0, staff, doctors.length + pharmacists.length + administrators.length, nurses.length);
    
        return staff;
    }

    /**
     * Removes a user by their ID.
     *
     * @param userId The ID of the user to remove.
     * @return null if successful, otherwise an error message.
     */
    public String removeUser(String userId) {
        try {
            User user = userService.findOne(userId, false, true);
            userService.removeUser(user);
            return null;
        } catch (Error e) {
            return "Something went wrong when removing the user!";
        }
    }

    /**
     * Adds a new user with the specified details.
     *
     * @param id     The ID of the new user.
     * @param role   The role of the new user (PHARMACIST, ADMINISTRATOR, DOCTOR, or NURSE).
     * @param name   The name of the new user.
     * @param age    The age of the new user.
     * @param gender The gender of the new user.
     * @return null if successful, otherwise an error message.
     */
    public String addUser(
        String id,
        UserRole role,
        String name,
        int age,
        Gender gender
    ) {
        try {
            byte[] salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(Constant.DEFAULT_PASSWORD, salt);
            LocalDateTime currentDate = LocalDateTime.now();

            switch (role) {
                case PHARMACIST:
                    Pharmacist pharmacist = new Pharmacist(
                        id,
                        hashedPassword,
                        salt,
                        name,
                        age,
                        gender,
                        currentDate,
                        currentDate
                    );
                    pharmacistRepository.save(pharmacist);
                case ADMINISTRATOR:
                    Administrator administrator = new Administrator(
                        id, 
                        hashedPassword, 
                        salt, 
                        name, 
                        age, 
                        gender, 
                        currentDate, 
                        currentDate
                    );
                    administratorRepository.save(administrator);
                case DOCTOR:
                    Doctor doctor = new Doctor(
                        id, 
                        hashedPassword, 
                        salt, 
                        name, 
                        age, 
                        gender,
                        Constant.DEFAULT_START_WORK_HOURS, 
                        Constant.DEFAULT_END_WORK_HOURS, 
                        currentDate, 
                        currentDate
                    );
                    doctorRepository.save(doctor);
                case NURSE:
                    Nurse nurse = new Nurse(
                        id,
                        hashedPassword,
                        salt,
                        name,
                        age,
                        gender,
                        currentDate,
                        currentDate
                    );
                    nurseRepository.save(nurse);
                default:
                    break;
            }
            return null;

        } catch (Exception e) {
            return "Something went wrong when creating a user";
        }
    }

    /**
     * Checks if a user ID is already in use.
     *
     * @param id The user ID to check.
     * @return true if the ID is already in use, false otherwise.
     */
    public boolean isDuplicateId(String id) {
        Administrator admin = administratorRepository.findOne(id);
        if (admin != null) return true;

        Doctor doctor = doctorRepository.findOne(id);
        if (doctor != null) return true;

        Pharmacist pharmacist = pharmacistRepository.findOne(id);
        if (pharmacist != null) return true;

        Nurse nurse = nurseRepository.findOne(id);
        if (nurse != null) return true;

        return false;
    }

    /**
     * Updates the name of a user by their ID.
     *
     * @param userId The ID of the user to update.
     * @param name   The new name for the user.
     * @return null if successful, otherwise an error message.
     */
    public String updateUserName(String userId, String name) {
        try {
            User user = userService.findOne(userId, false, true);
            user.setName(name);
            userService.updateUser(user);
            return null;
        } catch (Error e) {
            return "Something went wrong when updating the user!";
        }
    }

    /**
     * Updates the password of a user by their ID.
     *
     * @param userId   The ID of the user to update.
     * @param password The new password for the user.
     * @return null if successful, otherwise an error message.
     */
    public String updateUserPassword(String userId, String password) {
        try {
            User user = userService.findOne(userId, false, true);
            user.setPassword(password);
            userService.updateUser(user);
            return null;
        } catch (Exception e) {
            return "Something went wrong when updating the password!";
        }
    }

    /**
     * Updates the age of a user by their ID.
     *
     * @param userId The ID of the user to update.
     * @param age    The new age for the user.
     * @return null if successful, otherwise an error message.
     */
    public String updateUserAge(String userId, int age) {
        try {
            User user = userService.findOne(userId, false, true);
            user.setAge(age);
            userService.updateUser(user);
            return null;
        } catch (Exception e) {
            return "Something went wrong when updating the age!";
        }
    }
}
