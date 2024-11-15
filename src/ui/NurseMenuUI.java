package ui;

/**
 * The NurseMenuUI class provides the user interface for the nurse's menu.
 * It extends the MenuUI class and displays the available options specific to nurses.
 * 
 * @author Tan Jou Yuan
 * @version 1.0
 */
public class NurseMenuUI extends MenuUI {

    /**
     * Prints the options available in the nurse menu.
     * These options include managing patients, viewing appointments, recording patient vitals, and logging out.
     */
    public void printOptions() {
        printMenu("Nurse Menu",
            "1. View and Manage Patients",
            "2. View Today's Appointments",
            "3. Record Patient Vital",
            "4. Logout"
        );
    }
}
