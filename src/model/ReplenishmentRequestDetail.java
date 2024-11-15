package model;

import enums.ReplenishmentRequestStatus;
import java.time.LocalDateTime;

/**
 * The ReplenishmentRequestDetail class extends ReplenishmentRequest, providing additional details
 * about the specific medicine and pharmacist involved in the request.
 * 
 * @author Russell Arvin 
 * @version 1.0 
 */
public class ReplenishmentRequestDetail extends ReplenishmentRequest {

    private Medicine medicine;
    private Pharmacist pharmacist;

    /**
     * Constructs a ReplenishmentRequestDetail with the specified details, including medicine and pharmacist information.
     *
     * @param id            The unique ID of the replenishment request.
     * @param medicine      The medicine associated with the replenishment request.
     * @param pharmacist    The pharmacist who made the replenishment request.
     * @param newAmount     The amount of medicine requested for replenishment.
     * @param status        The current status of the replenishment request.
     * @param createdAt     The date and time when the request was created.
     * @param updatedAt     The date and time when the request was last updated.
     */
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

    /**
     * Gets the medicine associated with this replenishment request.
     *
     * @return The Medicine object associated with the request.
     */
    public Medicine getMedicine() {
        return medicine;
    }

    /**
     * Gets the pharmacist who made this replenishment request.
     *
     * @return The Pharmacist object associated with the request.
     */
    public Pharmacist getPharmacist() {
        return pharmacist;
    }

    /**
     * Sets the medicine associated with this replenishment request.
     *
     * @param medicine The Medicine object to associate with the request.
     */
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    /**
     * Sets the pharmacist associated with this replenishment request.
     *
     * @param pharmacist The Pharmacist object to associate with the request.
     */
    public void setPharmacist(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
    }

    /**
     * Creates a ReplenishmentRequestDetail object from an existing ReplenishmentRequest, associating it
     * with the specified medicine and pharmacist details.
     *
     * @param request     The existing ReplenishmentRequest object to use as a base.
     * @param medicine    The medicine associated with the replenishment request.
     * @param pharmacist  The pharmacist who made the replenishment request.
     * @return A new ReplenishmentRequestDetail object with medicine and pharmacist information.
     */
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
