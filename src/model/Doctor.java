package model;

import enums.Gender;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * The Doctor class represents a doctor in the system, extending the User class
 * with additional attributes for working hours.
 * 
 * @author Natalyn Pong 
 * @version 1.0 
 */
public class Doctor extends User {

    private int startWorkHours;
    private int endWorkHours;

    /**
     * Constructs a Doctor with the specified details, including work hours.
     *
     * @param id            The unique ID of the doctor.
     * @param password      The password of the doctor.
     * @param salt          The salt used for password hashing.
     * @param name          The name of the doctor.
     * @param age           The age of the doctor.
     * @param gender        The gender of the doctor.
     * @param startWorkHours The starting hour of the doctor's work schedule.
     * @param endWorkHours   The ending hour of the doctor's work schedule.
     * @param createdAt     The date and time when the doctor was created.
     * @param updatedAt     The date and time when the doctor was last updated.
     */
    public Doctor(
        String id,
        String password,
        byte[] salt,
        String name,
        int age,
        Gender gender,
        int startWorkHours,
        int endWorkHours,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, password, salt, name, age, gender, createdAt, updatedAt);
        this.startWorkHours = startWorkHours;
        this.endWorkHours = endWorkHours;
    }

    /**
     * Gets the starting hour of the doctor's work schedule.
     *
     * @return The start work hour.
     */
    public int getStartWorkHours() {
        return this.startWorkHours;
    }

    /**
     * Sets the work hours for the doctor.
     *
     * @param startWorkHours The starting hour of the doctor's work schedule.
     * @param endWorkHours   The ending hour of the doctor's work schedule.
     */
    public void setWorkHours(
        int startWorkHours,
        int endWorkHours
    ) {
        this.startWorkHours = startWorkHours;
        this.endWorkHours = endWorkHours;
    }

    /**
     * Gets the ending hour of the doctor's work schedule.
     *
     * @return The end work hour.
     */
    public int getEndWorkHours() {
        return this.endWorkHours;
    }

    /**
     * Returns a CSV string representation of the doctor, including work hours and other attributes.
     *
     * @return A CSV-formatted string containing doctor data.
     */
    @Override
    public String toCsvString() {
        return String.join(",",
            id,
            password,
            new String(salt, StandardCharsets.UTF_8), // from salt
            name,
            String.valueOf(age),
            gender.toString(),
            String.valueOf(startWorkHours),
            String.valueOf(endWorkHours),
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
