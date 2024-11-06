package ui;

public class AdministratorMenuUI extends MenuUI {
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