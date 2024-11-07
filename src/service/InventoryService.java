package service;

import model.Medicine;
import repository.MedicineRepository;

public class InventoryService {
    private MedicineRepository medicineRepository;

    public InventoryService(
        MedicineRepository medicineRepository
    ) {
        this.medicineRepository = medicineRepository;
    }

    public Medicine[] getAllMedicines(){
        return this.medicineRepository.findAll();
    }

    public String updateStock(
        String medicineId,
        int newStock
    ) {
        Medicine medicine = this.medicineRepository.findOne(medicineId);
        if(medicine == null){
            return "Medicine not found";
        }
        medicine.setStock(newStock);
        this.medicineRepository.update(medicine);
        return null;
    }

    public String updateLowStockAlert(
        String medicineId,
        int newStock
    ) {
        Medicine medicine = this.medicineRepository.findOne(medicineId);
        if(medicine == null){
            return "Medicine not found";
        }
        medicine.setLowStockAlert(newStock);
        this.medicineRepository.update(medicine);
        return null;
    }

    public String deleteMedicine(
        String medicineId
    ) {
        Medicine medicine = this.medicineRepository.findOne(medicineId);
        if(medicine == null) return "Medicine not found";
        this.medicineRepository.delete(medicineId);
        return null;
    }

    public String addMedicine(
        String name,
        int stock,
        int alertLevel
    ){  
        System.out.println("HERE");
        Medicine medicine = new Medicine(name, stock, alertLevel);
        this.medicineRepository.save(medicine);
        return null;
    }

}
