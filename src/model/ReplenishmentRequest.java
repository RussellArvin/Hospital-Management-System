package model;

import enums.ReplenishmentRequestStatus;
import java.time.LocalDateTime;

/**
 * The ReplenishmentRequest class represents a request to replenish the stock of a specific medicine.
 * It includes details such as the medicine, the pharmacist making the request, the requested amount, and the request status.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0 
 */
public class ReplenishmentRequest extends BaseEntity {

    private String medicineId;
    private String pharmacistId;
    private int newAmount;
    ReplenishmentRequestStatus status;

    /**
     * Constructs a ReplenishmentRequest with the specified details, including ID and timestamps.
     *
     * @param id            The unique ID of the replenishment request.
     * @param medicineId    The ID of the medicine to be replenished.
     * @param pharmacistId  The ID of the pharmacist making the request.
     * @param newAmount     The amount of medicine requested for replenishment.
     * @param status        The current status of the replenishment request.
     * @param createdAt     The date and time when the request was created.
     * @param updatedAt     The date and time when the request was last updated.
     */
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

    /**
     * Constructs a ReplenishmentRequest with pending status and without specifying ID and timestamps.
     *
     * @param medicineId    The ID of the medicine to be replenished.
     * @param pharmacistId  The ID of the pharmacist making the request.
     * @param newAmount     The amount of medicine requested for replenishment.
     */
    public ReplenishmentRequest(
        String medicineId,
        String pharmacistId,
        int newAmount
    ) {
        super();
        this.medicineId = medicineId;
        this.pharmacistId = pharmacistId;
        this.newAmount = newAmount;
        this.status = ReplenishmentRequestStatus.PENDING;
    }

    /**
     * Gets the amount of medicine requested for replenishment.
     *
     * @return The requested amount.
     */
    public int getNewAmount() {
        return this.newAmount;
    }

    /**
     * Gets the ID of the medicine to be replenished.
     *
     * @return The medicine ID.
     */
    public String getMedicineId() {
        return this.medicineId;
    }

    /**
     * Gets the ID of the pharmacist making the replenishment request.
     *
     * @return The pharmacist ID.
     */
    public String getPharmacistId() {
        return this.pharmacistId;
    }

    /**
     * Gets the current status of the replenishment request.
     *
     * @return The status of the request.
     */
    public ReplenishmentRequestStatus getStatus() {
        return this.status;
    }
    
    /**
     * Sets the new amount of medicine requested for replenishment.
     *
     * @param newAmount The new amount to set.
     */
    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }

    /**
     * Sets the status of the replenishment request.
     *
     * @param status The status to set for the request.
     */
    public void setStatus(ReplenishmentRequestStatus status) {
        this.status = status;
    }

    /**
     * Returns a CSV string representation of the replenishment request, including all relevant attributes.
     *
     * @return A CSV-formatted string containing replenishment request data.
     */
    public String toCsvString() {
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
