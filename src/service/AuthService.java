package service;

import model.User;

/**
 * The AuthService class provides authentication and password management functionalities.
 * It integrates with the UserService to validate user credentials and update passwords.
 * 
 * @author Russell Arvin 
 * @version 1.0
 */
public class AuthService {
    private UserService userService;

    /**
     * Constructs an AuthService with the given UserService dependency.
     *
     * @param userService The service used to manage user data and operations.
     */
    public AuthService(
        UserService userService
    ) {
        this.userService = userService;
    }

    /**
     * Updates the password for a given user.
     *
     * @param user     The user whose password is being updated.
     * @param password The new password to set.
     * @return null if successful, otherwise an error message.
     */
    public String setPassword(User user, String password) {
        try {
            user.setPassword(password);
            userService.updateUser(user);
            return null;
        } catch (Exception e) {
            System.out.println(e.toString());
            return "Something went wrong when updating password";
        }
    }

    /**
     * Authenticates a user based on their ID and password.
     *
     * @param id       The ID of the user.
     * @param password The password of the user.
     * @param isStaff  Indicates whether the user is a staff member.
     * @return The authenticated User object if credentials are valid, or null if invalid.
     */
    public User Login(String id, String password, boolean isStaff) {
        if (id == null || password == null) {
            return null;
        }

        User user = userService.findOne(id, !isStaff, isStaff);
        if (user != null && user.validatePassword(password)) {
            return user;
        } else if (user != null && !user.validatePassword(password)) {
            return null;
        }

        return null;
    }
}
