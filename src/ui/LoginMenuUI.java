package ui;

public class LoginMenuUI extends MenuUI {
    public void printOptions(){
        printMenu("Hospital Management System",
            "1. Login as Patient",
            "2. Register as Patient",
            "3. Login as Staff",
            "4. Exit"
        );
    }
}
