package ui;

/**
 * The AdministratorMenuUI class provides the menu options specific to administrators.
 * It extends the {@link MenuUI} class and customizes the menu for administrator functionalities.
 * 
 * @author Lim Jun Howe 
 * @version 1.0
 */
public class AdministratorMenuUI extends MenuUI {

    /**
     * Prints the menu options available to administrators.
     * The menu includes options for managing hospital staff, viewing appointment details,
     * managing medication inventory, approving replenishment requests, and logging out.
     */
    public void printOptions() {
        printMenu("Administrator Menu",
            "1. View and Manage Hospital Staff",
            "2. View Appointments Details",
            "3. View and Manage Medication Inventory",
            "4. Approve Replenishment Requests",
            "5. Logout"
        );
    }
}
