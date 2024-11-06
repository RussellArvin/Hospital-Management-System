package ui;

public class PatientMenuUI extends MenuUI {
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