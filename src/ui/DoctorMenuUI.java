package ui;

public class DoctorMenuUI extends MenuUI {
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