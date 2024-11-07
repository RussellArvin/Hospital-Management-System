package service;

import model.Administrator;
import model.Doctor;
import model.Pharmacist;
import model.User;
import repository.AdministratorRepository;
import repository.DoctorRepository;
import repository.PatientRepository;
import repository.PharmacistRepository;

public class StaffService {
    private DoctorRepository doctorRepository;
    private PharmacistRepository pharmacistRepository;
    private AdministratorRepository administratorRepository;
    private UserService userService;

    public StaffService(
        DoctorRepository doctorRepository,
        PharmacistRepository pharmacistRepository,
        AdministratorRepository administratorRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.administratorRepository = administratorRepository;
        this.userService = new UserService(administratorRepository, pharmacistRepository, doctorRepository, new PatientRepository());
    }



    public User[] getAllStaffData() {
        Doctor[] doctors = this.doctorRepository.findAll();
        Pharmacist[] pharmacists = this.pharmacistRepository.findAll();
        Administrator[] administrators = this.administratorRepository.findAll();
    
        User[] staff = new User[doctors.length + pharmacists.length + administrators.length];
    
        // Copy all three arrays
        System.arraycopy(doctors, 0, staff, 0, doctors.length);
        System.arraycopy(pharmacists, 0, staff, doctors.length, pharmacists.length);
        System.arraycopy(administrators, 0, staff, doctors.length + pharmacists.length, administrators.length);
    
        return staff;
    }

    public void removeUser(String userId){
        User user = userService.findOne
    }
}
