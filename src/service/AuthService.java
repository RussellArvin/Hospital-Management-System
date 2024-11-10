package service;

import model.User;
import repository.AdministratorRepository;
import repository.DoctorRepository;
import repository.PatientRepository;
import repository.PharmacistRepository;

public class AuthService {
    private UserService userService;

    public AuthService(
        PatientRepository patientRepository,
        DoctorRepository doctorRepository,
        PharmacistRepository pharmacistRepository,
        AdministratorRepository administratorRepository
    ) {
        this.userService = new UserService(administratorRepository, pharmacistRepository, doctorRepository, patientRepository);
    }

    public User Login(String id, String password, boolean isStaff) {
        if (id == null || password == null) {
            return null;
        }

        User user = userService.findOne(id,!isStaff,isStaff);
        if(user != null && user.validatePassword(password)){
            return user;
        }
        else if(user!= null && !user.validatePassword(password)){
            return null;
        }

        return null;
    }
}
