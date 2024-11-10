package model;

import java.time.LocalDateTime;

import enums.PrescriptionStatus;

public class PrescriptionWithMedicine extends Prescription {
    private Medicine medicine;

    public PrescriptionWithMedicine(
        String id,
        String appointmentOutcomeId,
        String medicineId,
        PrescriptionStatus status,
        int amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Medicine medicine
    ){
        super(id, appointmentOutcomeId, medicineId, status, amount, createdAt, updatedAt);
        this.medicine = medicine;
    }

    public Medicine getMedicine(){
        return this.medicine;
    }

    public static PrescriptionWithMedicine fromPrescription(
        Prescription prescription,
        Medicine medicine
    ){
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
