package service;

import java.time.LocalDateTime;

import enums.Gender;
import enums.UserRole;
import model.Administrator;
import model.Doctor;
import model.Pharmacist;
import model.User;
import model.Nurse;
import repository.AdministratorRepository;
import repository.DoctorRepository;
import repository.NurseRepository;
import repository.PatientRepository;
import repository.PharmacistRepository;
import util.Constant;
import util.PasswordUtil;

public class StaffService {
    private DoctorRepository doctorRepository;
    private PharmacistRepository pharmacistRepository;
    private AdministratorRepository administratorRepository;
    private NurseRepository nurseRepository;
    private UserService userService;

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

    public User[] getAllStaffData() {
        Doctor[] doctors = this.doctorRepository.findAll();
        Pharmacist[] pharmacists = this.pharmacistRepository.findAll();
        Administrator[] administrators = this.administratorRepository.findAll();
        Nurse[] nurses = this.nurseRepository.findAll();
    
        User[] staff = new User[doctors.length + pharmacists.length + administrators.length + nurses.length];
    
        // Copy all arrays
        System.arraycopy(doctors, 0, staff, 0, doctors.length);
        System.arraycopy(pharmacists, 0, staff, doctors.length, pharmacists.length);
        System.arraycopy(administrators, 0, staff, doctors.length + pharmacists.length, administrators.length);
        System.arraycopy(nurses, 0, staff, doctors.length + pharmacists.length + administrators.length, nurses.length);
    
        return staff;
    }

    public String removeUser(String userId){
        try{
            User user = userService.findOne(userId,false, true);
            userService.removeUser(user);
            return null;
        }
        catch(Error e){
            return "Something went wrong when removing the user!";
        }
    }

    public String addUser(
        String id,
        UserRole role,
        String name,
        int age,
        Gender gender
    ) {
        try{
            byte[] salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(Constant.DEFAULT_PASSWORD, salt);
            LocalDateTime currentDate = LocalDateTime.now();

            switch(role){
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

        } catch(Exception e){
            return "Something went wrong when creating a user";
        }
    }

    public boolean isDuplicateId(String id){
        Administrator admin = administratorRepository.findOne(id);
        if (admin != null) return true;

        Doctor doctor = doctorRepository.findOne(id);
        if (doctor != null) return true;

        Pharmacist pharmacist = pharmacistRepository.findOne(id);
        if (pharmacist != null) return true;

        Nurse nurse = nurseRepository.findOne(id);
        if(nurse != null) return true;

        return false;
    }

    public String updateUserName(String userId, String name){
        try{
            User user = userService.findOne(userId,false,true);
            user.setName(name);
            userService.updateUser(user);
            return null;
        } catch(Error e){
            return "Something went wrong when updating the user!";
        }
    }

    public String updateUserPassword(String userId, String password){
        try{
            User user = userService.findOne(userId, false, true);
            user.setPassword(password);
            userService.updateUser(user);
            return null;
        } catch(Exception e){
            return "Something went wrong when updating the password!";
        }

    }

    public String updateUserAge(String userId, int age){
        try{
            User user = userService.findOne(userId,false,true);
            user.setAge(age);
            userService.updateUser(user);
            return null;
        } catch(Exception e){
            return "Something went wrong when updating the age!";
        }
    }
}
