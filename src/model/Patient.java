package model;

import enums.BloodType;
import enums.Gender;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The Patient class represents a patient in the system, extending the User class.
 * It includes additional details specific to a patient, such as date of birth, blood type,
 * contact information, and other identifying details.
 * 
 * @author Lim Jun Howe 
 * @version 1.0 
 */
public class Patient extends User {

    private LocalDate dateOfBirth;
    private BloodType bloodType;
    private int phoneNumber;
    private String email;

    /**
     * Constructs a Patient with the specified details.
     *
     * @param id           The unique ID of the patient.
     * @param password     The password of the patient.
     * @param salt         The salt used for password hashing.
     * @param name         The name of the patient.
     * @param age          The age of the patient.
     * @param dateOfBirth  The date of birth of the patient.
     * @param gender       The gender of the patient.
     * @param bloodType    The blood type of the patient.
     * @param phoneNumber  The contact phone number of the patient.
     * @param email        The email address of the patient.
     */
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
    ) {
        super(id, password, salt, name, age, gender);
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    /**
     * Constructs a Patient with detailed information including timestamps.
     *
     * @param id           The unique ID of the patient.
     * @param password     The password of the patient.
     * @param salt         The salt used for password hashing.
     * @param name         The name of the patient.
     * @param age          The age of the patient.
     * @param dateOfBirth  The date of birth of the patient.
     * @param gender       The gender of the patient.
     * @param bloodType    The blood type of the patient.
     * @param phoneNumber  The contact phone number of the patient.
     * @param email        The email address of the patient.
     * @param createdAt    The date and time when the patient record was created.
     * @param updatedAt    The date and time when the patient record was last updated.
     */
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
    ) {
        super(id, password, salt, name, age, gender, createdAt, updatedAt);
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * Sets the contact phone number of the patient.
     *
     * @param phoneNumber The contact phone number to set.
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the email address of the patient.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the contact phone number of the patient.
     *
     * @return The phone number of the patient.
     */
    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Gets the email address of the patient.
     *
     * @return The email address of the patient.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the blood type of the patient.
     *
     * @return The blood type of the patient.
     */
    public BloodType getBloodType() {
        return this.bloodType;
    }

    /**
     * Returns a CSV string representation of the patient, including all relevant attributes.
     *
     * @return A CSV-formatted string containing patient data.
     */
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

    /**
     * Gets the date of birth of the patient.
     *
     * @return The date of birth of the patient.
     */
    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }
}
