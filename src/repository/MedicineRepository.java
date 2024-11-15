package repository;

import model.Medicine;
import repository.base.CsvRepository;
import repository.mapper.MedicineMapper;

/**
 * The MedicineRepository class extends CsvRepository for managing Medicine entities.
 * It specifies the CSV file and header used for Medicine data and provides methods
 * for accessing and managing Medicine records based on their name.
 * 
 * @author Russell Arvin 
 * @version 1.0
 */
public class MedicineRepository extends CsvRepository<Medicine, MedicineMapper> {

    private static final String CSV_FILE = "data/medicine.csv";
    private static final String CSV_HEADER = "id,name,stock,lowStockAlert,createdAt,updatedAt";

    /**
     * Constructs a MedicineRepository with predefined CSV file and header information
     * for storing Medicine data and initializes the MedicineMapper.
     */
    public MedicineRepository() {
        super(CSV_FILE, CSV_HEADER, new MedicineMapper());
    }

    /**
     * Retrieves all Medicine records from the CSV file.
     *
     * @return An array of all Medicine objects in the repository.
     */
    @Override
    public Medicine[] findAll() {
        return super.findAll(Medicine.class);
    }

    /**
     * Finds and returns a Medicine record by the specified name.
     *
     * @param name The name of the medicine to search for.
     * @return The Medicine object with the specified name, or null if not found.
     */
    public Medicine findOneByName(String name) {
        String line = this.fileManager.findLineByColumnValue("name", name);
        
        if (line == null) return null;
        return mapper.fromCsvString(line);
    }
}
