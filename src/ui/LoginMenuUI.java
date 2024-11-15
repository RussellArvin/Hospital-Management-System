package ui;

/**
 * The LoginMenuUI class provides a user interface for the login menu of the Hospital Management System.
 * It extends the MenuUI class and displays the available login and registration options for users.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class LoginMenuUI extends MenuUI {

    /**
     * Prints the options available in the login menu.
     * Users can choose to login as a patient or staff, register as a patient, or exit the system.
     */
    public void printOptions() {
        printMenu("Hospital Management System",
            "1. Login as Patient",
            "2. Register as Patient",
            "3. Login as Staff",
            "4. Exit"
        );
    }
}
