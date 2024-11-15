package ui;

/**
 * The PharmacistMenuUI class provides the user interface for the pharmacist's menu.
 * It extends the MenuUI class and displays the available options specific to pharmacists.
 * 
 * @author Lim Jun Howe
 * @version 1.0
 */
public class PharmacistMenuUI extends MenuUI {

    /**
     * Prints the options available in the pharmacist menu.
     * These options include viewing appointment outcomes, managing prescription statuses,
     * checking medication inventory, submitting replenishment requests, and logging out.
     */
    public void printOptions() {
        printMenu("Pharmacist Menu",
            "1. View Appointment Outcome Record",
            "2. Update Prescription Status",
            "3. View Medication Inventory",
            "4. Submit Replenishment Request",
            "5. Logout"
        );
    }
}
