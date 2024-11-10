package service;

import model.User;

public class AuthService {
    private UserService userService;

    public AuthService(
        UserService userService
    ) {
        this.userService = userService;
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
