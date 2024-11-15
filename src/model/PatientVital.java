package model;

import java.time.LocalDateTime;

/**
 * The PatientVital class represents the vital signs of a patient, including
 * measurements such as blood oxygen level, height, weight, and blood pressure.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0
 */
public class PatientVital extends BaseEntity {

    private String patientId;
    private int bloodOxygen;
    private int height;
    private int weight;
    private int bloodPressure;

    /**
     * Constructs a PatientVital with the specified details.
     *
     * @param patientId     The unique ID of the patient associated with these vitals.
     * @param bloodOxygen   The blood oxygen level of the patient.
     * @param height        The height of the patient.
     * @param weight        The weight of the patient.
     * @param bloodPressure The blood pressure of the patient.
     */
    public PatientVital(
        String patientId,
        int bloodOxygen,
        int height,
        int weight,
        int bloodPressure
    ) {
        super();
        this.patientId = patientId;
        this.bloodOxygen = bloodOxygen;
        this.height = height;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
    }

    /**
     * Constructs a PatientVital with detailed information including ID and timestamps.
     *
     * @param id            The unique ID of the patient vital record.
     * @param patientId     The unique ID of the patient associated with these vitals.
     * @param bloodOxygen   The blood oxygen level of the patient.
     * @param height        The height of the patient.
     * @param weight        The weight of the patient.
     * @param bloodPressure The blood pressure of the patient.
     * @param createdAt     The date and time when the record was created.
     * @param updatedAt     The date and time when the record was last updated.
     */
    public PatientVital(
        String id,
        String patientId,
        int bloodOxygen,
        int height,
        int weight,
        int bloodPressure,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(patientId, createdAt, updatedAt);
        this.patientId = patientId;
        this.bloodOxygen = bloodOxygen;
        this.height = height;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
    }

    /**
     * Gets the ID of the patient associated with these vitals.
     *
     * @return The patient's ID.
     */
    public String getPatientId() {
        return this.patientId;
    }

    /**
     * Gets the blood oxygen level of the patient.
     *
     * @return The blood oxygen level.
     */
    public int getBloodOxygen() {
        return this.bloodOxygen;
    }

    /**
     * Gets the height of the patient.
     *
     * @return The height of the patient.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Gets the weight of the patient.
     *
     * @return The weight of the patient.
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Gets the blood pressure of the patient.
     *
     * @return The blood pressure of the patient.
     */
    public int getBloodPressure() {
        return this.bloodPressure;
    }

    /**
     * Returns a CSV string representation of the patient's vital data, including all relevant attributes.
     *
     * @return A CSV-formatted string containing patient vital data.
     */
    public String toCsvString() {
        return String.join(",",
            id,
            patientId,
            String.valueOf(bloodOxygen),
            String.valueOf(height),
            String.valueOf(weight),
            String.valueOf(bloodPressure),
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
