package ui;

/**
 * The PatientMenuUI class provides the user interface for the patient's menu.
 * It extends the MenuUI class and displays the available options specific to patients.
 * 
 * @author Russell Arvin 
 * @version 1.0
 */
public class PatientMenuUI extends MenuUI {

    /**
     * Prints the options available in the patient menu.
     * These options include viewing and managing medical records, appointments,
     * scheduling or canceling appointments, and logging out.
     */
    public void printOptions() {
        printMenu("Patient Menu",
            "1. View Medical Record",
            "2. Update Personal Information", 
            "3. View Available Appointment Slots",
            "4. Schedule an Appointment",
            "5. Reschedule an Appointment", 
            "6. Cancel an Appointment",
            "7. View Scheduled Appointments",
            "8. View Past Appointment Outcome Records",
            "9. Logout"
        );
    }
}
