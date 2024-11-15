package model;

/**
 * The PendingPrescription class represents a prescription that is yet to be fulfilled.
 * It includes details about the medicine and the amount prescribed.
 * 
 * @author Russell Arvin 
 * @version 1.0 
 */
public class PendingPrescription {

    private String medicineId;
    private int amount;

    /**
     * Constructs a PendingPrescription with the specified medicine ID and amount.
     *
     * @param medicineId The unique ID of the prescribed medicine.
     * @param amount     The amount of medicine prescribed.
     */
    public PendingPrescription(
        String medicineId,
        int amount
    ) {
        this.medicineId = medicineId;
        this.amount = amount;
    }

    /**
     * Gets the ID of the prescribed medicine.
     *
     * @return The medicine ID.
     */
    public String getMedicineId() {
        return this.medicineId;
    }

    /**
     * Gets the amount of medicine prescribed.
     *
     * @return The amount prescribed.
     */
    public int getAmount() {
        return this.amount;
    }
}
