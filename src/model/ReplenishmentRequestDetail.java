package model;

import java.time.LocalDateTime;
import enums.ReplenishmentRequestStatus;

//TODO: REFACTOR THIS

public class ReplenishmentRequestDetail extends BaseEntity {
    private Medicine medicine;
    private Pharmacist pharmacist;
    private int newAmount;
    private ReplenishmentRequestStatus status;

    public ReplenishmentRequestDetail(
        String id,
        Medicine medicine,
        Pharmacist pharmacist,
        int newAmount,
        ReplenishmentRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.medicine = medicine;
        this.pharmacist = pharmacist;
        this.newAmount = newAmount;
        this.status = status;
    }

    public ReplenishmentRequestDetail(
        Medicine medicine,
        Pharmacist pharmacist,
        int newAmount,
        ReplenishmentRequestStatus status
    ){
        super();
        this.medicine = medicine;
        this.pharmacist = pharmacist;
        this.newAmount = newAmount;
        this.status = status;
    }

    // Getters
    public Medicine getMedicine() {
        return medicine;
    }

    public Pharmacist getPharmacist() {
        return pharmacist;
    }

    public int getNewAmount() {
        return newAmount;
    }

    public ReplenishmentRequestStatus getStatus() {
        return status;
    }

    // Setters
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public void setPharmacist(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
    }

    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }

    public void setStatus(ReplenishmentRequestStatus status) {
        this.status = status;
    }

    public String toCsvString() {
        return String.join(",",
            id,
            medicine.getId(),
            pharmacist.getId(),
            String.valueOf(newAmount),
            status.toString(),
            createdAt.toString(),
            updatedAt.toString()
        );
    }

    public static ReplenishmentRequestDetail fromReplenishmentRequest(
        ReplenishmentRequest request,
        Medicine medicine,
        Pharmacist pharmacist
    ) {
        return new ReplenishmentRequestDetail(
            request.getId(),
            medicine,
            pharmacist,
            request.getNewAmount(),
            request.getStatus(),
            request.getCreatedAt(),
            request.getUpdatedAt()
        );
    }
}