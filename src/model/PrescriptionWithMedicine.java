package model;

import enums.PrescriptionStatus;
import java.time.LocalDateTime;

/**
 * The PrescriptionWithMedicine class extends Prescription, providing additional details
 * about the medicine associated with the prescription.
 * 
 * @author Lim Jun Howe 
 * @version 1.0 
 */
public class PrescriptionWithMedicine extends Prescription {

    private Medicine medicine;

    /**
     * Constructs a PrescriptionWithMedicine with the specified details, including the associated medicine.
     *
     * @param id                  The unique ID of the prescription.
     * @param appointmentOutcomeId The ID of the appointment outcome associated with this prescription.
     * @param medicineId          The ID of the medicine prescribed.
     * @param status              The current status of the prescription.
     * @param amount              The amount of medicine prescribed.
     * @param createdAt           The date and time when the prescription was created.
     * @param updatedAt           The date and time when the prescription was last updated.
     * @param medicine            The medicine object associated with this prescription.
     */
    public PrescriptionWithMedicine(
        String id,
        String appointmentOutcomeId,
        String medicineId,
        PrescriptionStatus status,
        int amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Medicine medicine
    ) {
        super(id, appointmentOutcomeId, medicineId, status, amount, createdAt, updatedAt);
        this.medicine = medicine;
    }

    /**
     * Gets the medicine associated with this prescription.
     *
     * @return The Medicine object associated with this prescription.
     */
    public Medicine getMedicine() {
        return this.medicine;
    }

    /**
     * Creates a PrescriptionWithMedicine object from an existing Prescription, associating it with the specified medicine.
     *
     * @param prescription The existing Prescription object to use as a base.
     * @param medicine     The medicine associated with this prescription.
     * @return A new PrescriptionWithMedicine object with the specified medicine details.
     */
    public static PrescriptionWithMedicine fromPrescription(
        Prescription prescription,
        Medicine medicine
    ) {
        return new PrescriptionWithMedicine(
            prescription.getId(),
            prescription.getAppointmentOutcomeId(),
            prescription.getMedicineId(),
            prescription.getStatus(),
            prescription.getAmount(),
            prescription.getCreatedAt(),
            prescription.getUpdatedAt(),
            medicine
        );
    }
}
