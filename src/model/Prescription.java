package model;

import java.time.LocalDateTime;

import enums.PrescriptionStatus;

public class Prescription extends BaseEntity {
    private String appointmentOutcomeId;
    private String medicineId;
    private PrescriptionStatus status;
    private int amount;

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

    public void dispense(){
        this.status = PrescriptionStatus.DISPENSED;
    }

    public String getAppointmentOutcomeId(){
        return this.appointmentOutcomeId;
    }

    public String getMedicineId(){
        return this.medicineId;
    }

    public PrescriptionStatus getStatus(){
        return this.status;
    }

    public int getAmount(){
        return this.amount;
    }

    @Override
    public String toCsvString(){
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
