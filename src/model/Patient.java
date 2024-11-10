package model;

import enums.Gender;
import enums.BloodType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Patient extends User {
    private LocalDate dateOfBirth; 
    private BloodType bloodType;
    private int phoneNumber;
    private String email;


    public Patient(
        String id,
        String password,
        byte[] salt,
        String name, 
        int age,
        LocalDate dateOfBirth, 
        Gender gender, 
        BloodType bloodType,
        int phoneNumber,
        String email
    ){
        super(id,password,salt,name,age,gender);
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    public Patient(
        String id,
        String password,
        byte[] salt,
        String name,
        int age,
        LocalDate dateOfBirth,
        Gender gender,
        BloodType bloodType,
        int phoneNumber,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id, password, salt, name, age, gender, createdAt, updatedAt);
        this.dateOfBirth = dateOfBirth;
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

    public int getPhoneNumber(){
        return this.phoneNumber;
    }

    public String getEmail(){
        return this.email;
    }

    public BloodType getBloodType(){
        return this.bloodType;
    }

    @Override
    public String toCsvString() {
        return String.join(",",
            id,                         // from BaseEntity
            password,                   // from User
            new String(salt, StandardCharsets.UTF_8), // from salt
            name,                       // from User
            Integer.toString(age),
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