package model;

import java.time.LocalDateTime;

/**
 * The Medicine class represents a medication in the system, with attributes for name,
 * stock level, and a low stock alert threshold.
 * 
 * @author Celeste Ho 
 * @version 1.0 
 */
public class Medicine extends BaseEntity {

    private String name;
    private int stock;
    private int lowStockAlert;

    /**
     * Constructs a Medicine object with the specified details, including ID and timestamps.
     *
     * @param id           The unique ID of the medicine.
     * @param name         The name of the medicine.
     * @param stock        The current stock level of the medicine.
     * @param lowStockAlert The stock level threshold for low stock alerts.
     * @param createdAt    The date and time when the medicine was created.
     * @param updatedAt    The date and time when the medicine was last updated.
     */
    public Medicine(
        String id,
        String name,
        int stock,
        int lowStockAlert,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.stock = stock;
        this.lowStockAlert = lowStockAlert;
    }

    /**
     * Constructs a Medicine object with the specified name, stock level, and low stock alert threshold.
     *
     * @param name         The name of the medicine.
     * @param stock        The current stock level of the medicine.
     * @param lowStockAlert The stock level threshold for low stock alerts.
     */
    public Medicine(
        String name,
        int stock,
        int lowStockAlert
    ) {
        super();
        this.name = name;
        this.stock = stock;
        this.lowStockAlert = lowStockAlert;
    }

    /**
     * Gets the name of the medicine.
     *
     * @return The name of the medicine.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the current stock level of the medicine.
     *
     * @return The stock level.
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * Sets the stock level of the medicine.
     *
     * @param stock The new stock level.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the low stock alert threshold.
     *
     * @return The low stock alert level.
     */
    public int getLowStockAlert() {
        return this.lowStockAlert;
    }

    /**
     * Sets the low stock alert threshold.
     *
     * @param stock The new low stock alert level.
     */
    public void setLowStockAlert(int stock) {
        this.lowStockAlert = stock;
    }

    /**
     * Returns a CSV string representation of the medicine, including ID, name, stock level, alert threshold, and timestamps.
     *
     * @return A CSV-formatted string containing medicine data.
     */
    public String toCsvString() {
        return String.join(",",
            id,
            name,
            String.valueOf(stock),
            String.valueOf(lowStockAlert),
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
