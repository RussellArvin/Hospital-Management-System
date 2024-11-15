package ui;

/**
 * The DoctorMenuUI class provides the user interface for the doctor's menu.
 * It extends the MenuUI class and displays a list of options available to a doctor.
 * 
 * @author Russell Arvin 
 * @version 1.0 
 */
public class DoctorMenuUI extends MenuUI {

    /**
     * Prints the options available in the doctor menu.
     * These options allow the doctor to perform tasks such as viewing and updating
     * medical records, managing appointments, and logging out.
     */
    public void printOptions() {
        printMenu("Doctor Menu",
            "1. View Patient Medical Records",
            "2. Update Patient Medical Records",
            "3. View Personal Schedule",
            "4. Set Availability for Appointments",
            "5. Accept or Decline Appointment Requests",
            "6. View Upcoming Appointments",
            "7. Record Appointment Outcome",
            "8. Logout"
        );
    }
}
