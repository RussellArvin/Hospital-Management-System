package ui;

public class PharmacistMenuUI extends MenuUI {
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