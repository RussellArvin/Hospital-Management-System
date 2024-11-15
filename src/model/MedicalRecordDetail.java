package model;

import enums.MedicalRecordType;
import java.time.LocalDateTime;

/**
 * The MedicalRecordDetail class extends MedicalRecord, providing additional details
 * about the doctor associated with the medical record.
 * 
 * @author Russell Arvin 
 * @version 1.0 
 */
public class MedicalRecordDetail extends MedicalRecord {

    private Doctor doctor;

    /**
     * Constructs a MedicalRecordDetail with the specified details, including the associated doctor.
     *
     * @param id         The unique ID of the medical record.
     * @param patientId  The ID of the patient associated with this medical record.
     * @param doctorId   The ID of the doctor associated with this medical record.
     * @param type       The type of the medical record.
     * @param details    Specific details of the medical record.
     * @param createdAt  The date and time when the medical record was created.
     * @param updatedAt  The date and time when the medical record was last updated.
     * @param doctor     The doctor associated with this medical record.
     */
    public MedicalRecordDetail(
        String id,
        String patientId,
        String doctorId,
        MedicalRecordType type,
        String details,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Doctor doctor
    ) {
        super(id, patientId, doctorId, type, details, createdAt, updatedAt);
        this.doctor = doctor;
    }

    /**
     * Gets the doctor associated with this medical record.
     *
     * @return The Doctor associated with this medical record.
     */
    public Doctor getDoctor() {
        return this.doctor;
    }

    /**
     * Creates a MedicalRecordDetail object from an existing MedicalRecord, associating it with
     * the specified doctor details.
     *
     * @param record The existing MedicalRecord object to use as a base.
     * @param doctor The doctor associated with this medical record.
     * @return A new MedicalRecordDetail object with doctor details.
     */
    public static MedicalRecordDetail fromMedicalRecord(
        MedicalRecord record,
        Doctor doctor
    ) {
        return new MedicalRecordDetail(
            record.getId(),
            record.getPatientId(),
            record.getDoctorId(),
            record.getType(),
            record.getDetails(),
            record.getCreatedAt(),
            record.getUpdatedAt(),
            doctor    
        );
    }
}
