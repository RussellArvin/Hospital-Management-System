package model;

import enums.Gender;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import util.PasswordUtil;

/**
 * The User class serves as an abstract base class for user entities in the system.
 * It includes common attributes such as password, salt, name, age, and gender,
 * and provides utility methods for password validation and management.
 * 
 * @author Celeste Ho 
 * @version 1.0 
 */
public abstract class User extends BaseEntity {

    protected String password;
    protected byte[] salt;
    protected String name;
    protected int age;
    protected Gender gender;

    /**
     * Constructs a User object with the specified ID, password, salt, name, age, and gender.
     *
     * @param id       The unique identifier for the user.
     * @param password The hashed password of the user.
     * @param salt     The salt used for hashing the password.
     * @param name     The name of the user.
     * @param age      The age of the user.
     * @param gender   The gender of the user.
     */
    public User(
        String id, 
        String password,
        byte[] salt,
        String name,
        int age,
        Gender gender
    ) {
        super(id);
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    /**
     * Constructs a User object with the specified ID, password, salt, name, age, gender,
     * creation timestamp, and update timestamp.
     *
     * @param id        The unique identifier for the user.
     * @param password  The hashed password of the user.
     * @param salt      The salt used for hashing the password.
     * @param name      The name of the user.
     * @param age       The age of the user.
     * @param gender    The gender of the user.
     * @param createdAt The creation timestamp for the user.
     * @param updatedAt The last update timestamp for the user.
     */
    public User(
        String id,
        String password,
        byte[] salt,
        String name,
        int age,    
        Gender gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.salt = salt;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
    
    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The new name for the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the age of the user.
     *
     * @return The age of the user.
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Gets the gender of the user.
     *
     * @return The gender of the user.
     */
    public Gender getGender() {
        return this.gender;
    }

    /**
     * Validates the provided password against the stored hashed password and salt.
     *
     * @param password The plain-text password to validate.
     * @return true if the password matches, false otherwise.
     */
    public boolean validatePassword(String password) {
        try {
            return PasswordUtil.verifyPassword(password, this.password, this.salt);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sets a new password for the user, hashing it and generating a new salt.
     *
     * @param password The new plain-text password for the user.
     * @throws Exception If hashing or salt generation fails.
     */
    public void setPassword(String password) throws Exception {
        byte[] salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);
        this.password = hashedPassword;
        this.salt = salt;
    }

    /**
     * Sets the age of the user.
     *
     * @param age The new age for the user.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Returns a CSV representation of the user, including password and salt.
     *
     * @return A CSV-formatted string representing the user.
     */
    @Override
    public String toCsvString() {
        return String.join(",", 
            id, 
            password, 
            new String(salt, StandardCharsets.UTF_8), 
            name,
            String.valueOf(age),
            gender.toString(),
            createdAt.toString(), 
            updatedAt.toString()
        );
    }
}
