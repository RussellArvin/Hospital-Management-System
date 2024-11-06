package model;

import model.BaseEntity;
import enums.Gender;
import enums.BloodType;

import java.time.LocalDate;

public class Patient extends User {
    private LocalDate dateOfBirth; 
    private Gender gender;
    private BloodType bloodType;
    private int phoneNumber;
    private String email;


    public Patient(
        String id,
        String password, 
        String name, 
        LocalDate dateOfBirth, 
        Gender gender, 
        BloodType bloodType,
        int phoneNumber,
        String email
    ){
        super(id,password,name);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    Patient(
        String id,
        String name, 
        LocalDate dateOfBirth, 
        Gender gender, 
        BloodType bloodType,
        int phoneNumber,
        String email
    ){
        super(id,name);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email){
        this.email = email;
    }


    @Override
    public void displayMenu() {
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment");
        System.out.println("5. Reschedule an Appointment");
        System.out.println("6. Cancel an Appointment");
        System.out.println("7. View Scheduled Appointments");
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
    }

        @Override
    public String toCsvString() {
        return String.join(",",
            id,                         // from BaseEntity
            password,                   // from User
            name,                       // from User
            dateOfBirth.toString(),     // LocalDate to string
            gender.toString(),          // enum to string
            bloodType.toString(),       // enum to string
            String.valueOf(phoneNumber),// int to string
            email != null ? email : "", // handle potentially null email
            createdAt.toString(),       // from BaseEntity
            updatedAt.toString()        // from BaseEntity
        );
    }

        public LocalDate getDateOfBirth() {
            return this.dateOfBirth;
        }
}