package model;

import java.time.LocalDateTime;

import enums.ReplenishmentRequestStatus;

public class ReplenishmentRequest extends BaseEntity {
    private String medicineId;
    private String pharmacistId;
    private int newAmount;
    ReplenishmentRequestStatus status;

    public ReplenishmentRequest(
        String id,
        String medicineId,
        String pharmacistId,
        int newAmount,
        ReplenishmentRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.medicineId = medicineId;
        this.pharmacistId = pharmacistId;
        this.newAmount = newAmount;
        this.status = status;
    }

    public ReplenishmentRequest(
        String medicineId,
        String pharmacistId,
        int newAmount
    ){
        super();
        this.medicineId = medicineId;
        this.pharmacistId = pharmacistId;
        this.newAmount = newAmount;
        this.status = ReplenishmentRequestStatus.PENDING;
    }

    public int getNewAmount(){
        return this.newAmount;
    }

    public String getMedicineId(){
        return this.medicineId;
    }

    public String getPharmacistId(){
        return this.pharmacistId;
    }

    public ReplenishmentRequestStatus getStatus(){
        return this.status;
    }
    
    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }

    public void setStatus(ReplenishmentRequestStatus status){
        this.status = status;
    }

    public String toCsvString(){
        return String.join(",",
            id,
            medicineId,
            pharmacistId,
            String.valueOf(newAmount),
            status.toString(),
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
