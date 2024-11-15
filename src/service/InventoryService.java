package service;

import model.Medicine;
import repository.MedicineRepository;

/**
 * The InventoryService class manages inventory operations for medicines, including adding,
 * updating, retrieving, and deleting medicine records in the system.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class InventoryService {
    private MedicineRepository medicineRepository;

    /**
     * Constructs an InventoryService with the required MedicineRepository.
     *
     * @param medicineRepository Repository for managing medicine data.
     */
    public InventoryService(
        MedicineRepository medicineRepository
    ) {
        this.medicineRepository = medicineRepository;
    }

    /**
     * Retrieves all medicines in the inventory.
     *
     * @return An array of Medicine objects.
     */
    public Medicine[] getAllMedicines() {
        return this.medicineRepository.findAll();
    }

    /**
     * Updates the stock quantity for a specific medicine.
     *
     * @param medicineId The ID of the medicine to update.
     * @param newStock   The new stock quantity.
     * @return null if successful, otherwise an error message.
     */
    public String updateStock(
        String medicineId,
        int newStock
    ) {
        Medicine medicine = this.medicineRepository.findOne(medicineId);
        if (medicine == null) {
            return "Medicine not found";
        }
        medicine.setStock(newStock);
        this.medicineRepository.update(medicine);
        return null;
    }

    /**
     * Updates the low stock alert level for a specific medicine.
     *
     * @param medicineId The ID of the medicine to update.
     * @param newStock   The new low stock alert level.
     * @return null if successful, otherwise an error message.
     */
    public String updateLowStockAlert(
        String medicineId,
        int newStock
    ) {
        Medicine medicine = this.medicineRepository.findOne(medicineId);
        if (medicine == null) {
            return "Medicine not found";
        }
        medicine.setLowStockAlert(newStock);
        this.medicineRepository.update(medicine);
        return null;
    }

    /**
     * Deletes a specific medicine from the inventory.
     *
     * @param medicineId The ID of the medicine to delete.
     * @return null if successful, otherwise an error message.
     */
    public String deleteMedicine(
        String medicineId
    ) {
        Medicine medicine = this.medicineRepository.findOne(medicineId);
        if (medicine == null) return "Medicine not found";
        this.medicineRepository.delete(medicine);
        return null;
    }

    /**
     * Adds a new medicine to the inventory.
     *
     * @param name       The name of the medicine.
     * @param stock      The initial stock quantity.
     * @param alertLevel The low stock alert level.
     * @return null if successful, otherwise an error message.
     */
    public String addMedicine(
        String name,
        int stock,
        int alertLevel
    ) {  
        System.out.println("HERE");
        Medicine medicine = new Medicine(name, stock, alertLevel);
        this.medicineRepository.save(medicine);
        return null;
    }

    /**
     * Retrieves the ID of a medicine by its name.
     *
     * @param medicineName The name of the medicine to search.
     * @return The ID of the medicine if found, otherwise null.
     */
    public String getMedicineId(String medicineName) {
        try {
            Medicine medicine = this.medicineRepository.findOneByName(medicineName);
            if (medicine == null) return null;
            return medicine.getId();
        } catch (Exception e) {
            System.out.println("Something went wrong when checking existence of medicine!");
            return null;
        }
    }
}
