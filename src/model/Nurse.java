package model;

import enums.Gender;
import java.time.LocalDateTime;

/**
 * The Nurse class represents a nurse in the system, extending the User class.
 * It includes attributes inherited from User, such as id, password, name, age, gender, and timestamps.
 * 
 * @author Natalyn Pong 
 * @version 1.0 
 */
public class Nurse extends User {

    /**
     * Constructs a Nurse with the specified details.
     *
     * @param id         The unique ID of the nurse.
     * @param password   The password of the nurse.
     * @param salt       The salt used for password hashing.
     * @param name       The name of the nurse.
     * @param age        The age of the nurse.
     * @param gender     The gender of the nurse.
     * @param createdAt  The date and time when the nurse was created.
     * @param updatedAt  The date and time when the nurse was last updated.
     */
    public Nurse(
        String id,
        String password,
        byte[] salt,
        String name,
        int age,    
        Gender gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, password, salt, name, age, gender, createdAt, updatedAt);
    }
}
