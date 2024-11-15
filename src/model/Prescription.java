package model;

import enums.PrescriptionStatus;
import java.time.LocalDateTime;

/**
 * The Prescription class represents a prescription issued during a medical appointment,
 * including details such as the associated appointment outcome, medicine, status, and amount.
 * 
 * @author Natalyn Pong 
 * @version 1.0 
 */
public class Prescription extends BaseEntity {

    private String appointmentOutcomeId;
    private String medicineId;
    private PrescriptionStatus status;
    private int amount;

    /**
     * Constructs a Prescription with the specified details, including ID and timestamps.
     *
     * @param id                  The unique ID of the prescription.
     * @param appointmentOutcomeId The ID of the appointment outcome associated with this prescription.
     * @param medicineId          The ID of the medicine prescribed.
     * @param status              The current status of the prescription.
     * @param amount              The amount of medicine prescribed.
     * @param createdAt           The date and time when the prescription was created.
     * @param updatedAt           The date and time when the prescription was last updated.
     */
    public Prescription(
        String id,
        String appointmentOutcomeId,
        String medicineId,
        PrescriptionStatus status,
        int amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.appointmentOutcomeId = appointmentOutcomeId;
        this.medicineId = medicineId;
        this.status = status;
        this.amount = amount;
    }

    /**
     * Constructs a Prescription with a pending status from a pending prescription and an appointment outcome ID.
     *
     * @param appointmentOutcomeId The ID of the appointment outcome associated with this prescription.
     * @param pendingPrescription  The pending prescription details, including medicine ID and amount.
     */
    public Prescription(
        String appointmentOutcomeId,
        PendingPrescription pendingPrescription
    ) {
        super();
        this.appointmentOutcomeId = appointmentOutcomeId;
        this.status = PrescriptionStatus.PENDING;
        this.amount = pendingPrescription.getAmount();
        this.medicineId = pendingPrescription.getMedicineId();
    }

    /**
     * Marks the prescription as dispensed by updating its status to DISPENSED.
     */
    public void dispense() {
        this.status = PrescriptionStatus.DISPENSED;
    }

    /**
     * Gets the ID of the appointment outcome associated with this prescription.
     *
     * @return The appointment outcome ID.
     */
    public String getAppointmentOutcomeId() {
        return this.appointmentOutcomeId;
    }

    /**
     * Gets the ID of the medicine prescribed.
     *
     * @return The medicine ID.
     */
    public String getMedicineId() {
        return this.medicineId;
    }

    /**
     * Gets the current status of the prescription.
     *
     * @return The prescription status.
     */
    public PrescriptionStatus getStatus() {
        return this.status;
    }

    /**
     * Gets the amount of medicine prescribed.
     *
     * @return The prescribed amount.
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Returns a CSV string representation of the prescription, including all relevant attributes.
     *
     * @return A CSV-formatted string containing prescription data.
     */
    @Override
    public String toCsvString() {
        return String.join(",",
            id,
            appointmentOutcomeId,
            medicineId,
            status.toString(),
            String.valueOf(amount),
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
