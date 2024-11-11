package ui;

public class NurseMenuUI extends MenuUI {
    public void printOptions() {
        printMenu("Nurse Menu",
            "1. View and Manage Patients",
            "2. View Today's Appointments",
            "3. Logout"
        );
    }
}