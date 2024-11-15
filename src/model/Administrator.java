package model;

import enums.Gender;
import java.time.LocalDateTime;

/**
 * The Administrator class represents an administrator user in the system.
 * It extends the User class, inheriting basic user attributes and adding any
 * administrator-specific functionality or properties.
 * 
 * @author Lim Jun Howe 
 * @version 1.0
 */
public class Administrator extends User {

    /**
     * Constructs an Administrator object with the specified details.
     *
     * @param id The unique identifier for the administrator.
     * @param password The password of the administrator.
     * @param salt The salt used for password hashing.
     * @param name The name of the administrator.
     * @param age The age of the administrator.
     * @param gender The gender of the administrator.
     * @param createdAt The date and time when the administrator was created.
     * @param updatedAt The date and time when the administrator was last updated.
     */
    public Administrator(
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
