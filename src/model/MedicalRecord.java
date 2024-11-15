package model;

import enums.MedicalRecordType;
import java.time.LocalDateTime;

/**
 * The MedicalRecord class represents a medical record in the system, containing details
 * about the patient, doctor, record type, and specific information about the record.
 * 
 * @author Lim Jun Howe 
 * @version 1.0 
 */
public class MedicalRecord extends BaseEntity {

    private String patientId;
    private String doctorId;
    private MedicalRecordType type;
    private String details;

    /**
     * Constructs a MedicalRecord with the specified patient, doctor, type, and details.
     *
     * @param patientId The ID of the patient associated with this medical record.
     * @param doctorId  The ID of the doctor associated with this medical record.
     * @param type      The type of the medical record.
     * @param details   Specific details of the medical record.
     */
    public MedicalRecord(
        String patientId,
        String doctorId,
        MedicalRecordType type,
        String details
    ) {
        super();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.type = type;
        this.details = details;
    }

    /**
     * Constructs a MedicalRecord with detailed information including ID and timestamps.
     *
     * @param id        The unique ID of the medical record.
     * @param patientId The ID of the patient associated with this medical record.
     * @param doctorId  The ID of the doctor associated with this medical record.
     * @param type      The type of the medical record.
     * @param details   Specific details of the medical record.
     * @param createdAt The date and time when the medical record was created.
     * @param updatedAt The date and time when the medical record was last updated.
     */
    public MedicalRecord(
        String id,
        String patientId,
        String doctorId,
        MedicalRecordType type,
        String details,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.type = type;
        this.details = details;
    }

    /**
     * Gets the type of the medical record.
     *
     * @return The type of the medical record.
     */
    public MedicalRecordType getType() {
        return this.type;
    }

    /**
     * Gets the details of the medical record.
     *
     * @return The details of the medical record.
     */
    public String getDetails() {
        return this.details;
    }

    /**
     * Gets the ID of the patient associated with this medical record.
     *
     * @return The patient's ID.
     */
    public String getPatientId() {
        return this.patientId;
    }

    /**
     * Gets the ID of the doctor associated with this medical record.
     *
     * @return The doctor's ID.
     */
    public String getDoctorId() {
        return this.doctorId;
    }

    /**
     * Returns a CSV string representation of the medical record, including ID, patient ID, doctor ID, type, details, and timestamps.
     *
     * @return A CSV-formatted string containing medical record data.
     */
    public String toCsvString() {
        return String.join(",",
            id,
            patientId,
            doctorId,
            type.toString(),
            details,
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
