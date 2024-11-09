package model;

import java.time.LocalDateTime;

import enums.PrescriptionStatus;

public class Prescription extends BaseEntity {
    private String appointmentOutcomeId;
    private String medicineId;
    private PrescriptionStatus status;

    public Prescription(
        String id,
        String appointmentOutcomeId,
        String medicineId,
        PrescriptionStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.appointmentOutcomeId = appointmentOutcomeId;
        this.medicineId = medicineId;
        this.status = status;
    }

    public Prescription(
        String appointmentOutcomeId,
        String medicineId
    ) {
        super();
        this.appointmentOutcomeId = appointmentOutcomeId;
        this.medicineId = medicineId;
        this.status = PrescriptionStatus.PENDING;
    }

    public void dispense(){
        this.status = PrescriptionStatus.DISPENSED;
    }

    @Override
    public String toCsvString(){
        return String.join(",",
            id,
            appointmentOutcomeId,
            medicineId,
            status.toString(),
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
