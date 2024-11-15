package model;

import enums.Gender;
import java.time.LocalDateTime;

/**
 * The Pharmacist class represents a pharmacist in the system, extending the User class.
 * It includes attributes inherited from User, such as id, password, name, age, gender, and timestamps.
 * 
 * @author Celeste Ho 
 * @version 1.0 
 */
public class Pharmacist extends User {

    /**
     * Constructs a Pharmacist with the specified details.
     *
     * @param id         The unique ID of the pharmacist.
     * @param password   The password of the pharmacist.
     * @param salt       The salt used for password hashing.
     * @param name       The name of the pharmacist.
     * @param age        The age of the pharmacist.
     * @param gender     The gender of the pharmacist.
     * @param createdAt  The date and time when the pharmacist record was created.
     * @param updatedAt  The date and time when the pharmacist record was last updated.
     */
    public Pharmacist(
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
