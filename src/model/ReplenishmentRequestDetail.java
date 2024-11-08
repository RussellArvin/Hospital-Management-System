package model;

import java.time.LocalDateTime;
import enums.ReplenishmentRequestStatus;

//TODO: REFACTOR THIS

public class ReplenishmentRequestDetail extends ReplenishmentRequest {
    private Medicine medicine;
    private Pharmacist pharmacist;

    public ReplenishmentRequestDetail(
        String id,
        Medicine medicine,
        Pharmacist pharmacist,
        int newAmount,
        ReplenishmentRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, medicine.getId(), pharmacist.getId(), newAmount, status, createdAt, updatedAt);
        this.medicine = medicine;
        this.pharmacist = pharmacist;
    }

    // Getters
    public Medicine getMedicine() {
        return medicine;
    }

    public Pharmacist getPharmacist() {
        return pharmacist;
    }


    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public void setPharmacist(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
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