package service;

import model.User;

public class AuthService {
    private UserService userService;

    public AuthService(
        UserService userService
    ) {
        this.userService = userService;
    }

    public String setPassword(User user, String password){
        try{
            user.setPassword(password);
            userService.updateUser(user);
            return null;
            
        } catch(Exception e){
            return "Something went wrong when updating password";
        }
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
